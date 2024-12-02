package com.rohan.face.detection.media.scan.ui

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.rohan.face.detection.base.BaseNavActivity
import com.rohan.face.detection.media.scan.FaceDetectorHelper
import com.rohan.face.detection.media.scan.R
import com.rohan.face.detection.media.scan.databinding.ActivityPhotoFeedBinding
import com.rohan.face.detection.media.scan.di.AppComponentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.reflect.KClass

class PhotoFeedActivity : BaseNavActivity<PhotoFeedViewModel, PhotoFeedNavEvents>() {

    @Inject
    lateinit var photoFeedViewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityPhotoFeedBinding

    private lateinit var feedAdapter: FeedAdapter

    override fun getViewModelFactory(): ViewModelProvider.Factory = photoFeedViewModelFactory

    override fun getViewModelClass(): KClass<PhotoFeedViewModel> = PhotoFeedViewModel::class

    override fun onCreateNavAct(savedInstanceState: Bundle?) {
        setUpDI()
        setUpUI()
        observeEvents()
        viewModel.fetchAllFeeds()
        checkAndRequestPermission()
    }

    private fun setUpDI() {
        val appComponentProvider = applicationContext as AppComponentProvider
        // Get PhotoFeedComponent and inject dependencies
        val photoFeedComponent = appComponentProvider.getPhotoFeedComponent(this)
        photoFeedComponent.inject(this)
    }

    private fun setUpUI() {
        binding = ActivityPhotoFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        feedAdapter = FeedAdapter(this, supportFragmentManager, onNameTagUpdated = {
            viewModel.addNameTag(it)
        }, onLongClick = {
            launchFaceDetection(it.uri)
        })
        binding.feedGridView.apply {
            layoutManager = GridLayoutManager(this@PhotoFeedActivity, 3)
            adapter = feedAdapter
        }
    }

    private fun launchFaceDetection(uri: Uri) {
        val imageFragment = ImageDetectionFragment.newInstance(uri)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.frame_view,
            imageFragment
        ) // Replace with your container ID
        fragmentTransaction.addToBackStack(null) // Optional, if you want to allow back navigation
        fragmentTransaction.commit()
    }

    private fun observeEvents() {
        viewModel.showLoading.observe(
            this
        ) {
            binding.progressCircular.visibility = if (it) View.VISIBLE else View.GONE
        }

        lifecycleScope.launch {
            viewModel.feedList.collect { feedList ->
                // Check if the feed list is not null before updating the adapter
                if (!feedList.isNullOrEmpty()) {
                    feedAdapter.submitList(feedList) // Update the adapter with the new list
                }
            }
        }
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
            == PackageManager.PERMISSION_GRANTED
        ) {
            lazyLoadImagesFromGalley()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    private fun lazyLoadImagesFromGalley() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list: List<Uri> = fetchGalleryImages()
            withContext(Dispatchers.Main) {
                viewModel.updateNewFeedsFromGallery(list)
            }
        }
    }

    private fun fetchGalleryImages(): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection =
            "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ? AND ${MediaStore.Images.Media.MIME_TYPE} = ?"
        val selectionArgs = arrayOf("Camera", "image/jpeg")
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection, selectionArgs, null
        )
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                )
                imageUris.add(contentUri)
            }
        }
        return imageUris
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                lazyLoadImagesFromGalley()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
}