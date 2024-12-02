package com.rohan.face.detection.media.scan.ui

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rohan.face.detection.base.BaseNavVM
import com.rohan.face.detection.domain.PhotoFeedRepo
import com.rohan.face.detection.domain.model.FeedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotoFeedViewModel(
    private val repo: PhotoFeedRepo
) : BaseNavVM<PhotoFeedNavEvents>() {

    private val _feedList = MutableStateFlow<List<FeedModel>?>(null)
    val feedList: StateFlow<List<FeedModel>?> get() = _feedList

    private val _showLoading = MutableLiveData<Boolean>(false)
    val showLoading: LiveData<Boolean> get() = _showLoading

    fun fetchAllFeeds() {
        viewModelScope.launch(Dispatchers.IO) {
            _showLoading.postValue(true)
            _feedList.value = repo.getAllFeed()
            _showLoading.postValue(false)
        }
    }

    fun updateNewFeedsFromGallery(list: List<Uri>) {
        viewModelScope.launch(Dispatchers.IO) {
            val feedModelList = mutableListOf<FeedModel>()
            list.forEach {
                feedModelList.add(
                    FeedModel(
                        uri = it,
                        lastUpdatedTimeStamp = System.currentTimeMillis()
                    )
                )
            }
            _feedList.value = repo.insertFeeds(feedModelList)
        }
    }

    fun addNameTag(model: FeedModel){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateFeed(model)
        }
    }

    override fun onNavigatorVMEvent(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        event: PhotoFeedNavEvents
    ) {
        TODO("Not yet implemented")
    }

    /*

     private fun runDetectionOnImage(uri: Uri) {
         setUiEnabled(false)
         backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
         updateDisplayView(MediaType.IMAGE)
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
             val source = ImageDecoder.createSource(
                 baseContext.contentResolver,
                 uri
             )
             ImageDecoder.decodeBitmap(source)
         } else {
             MediaStore.Images.Media.getBitmap(
                 baseContext.contentResolver,
                 uri
             )
         }
             .copy(Bitmap.Config.ARGB_8888, true)
             ?.let { bitmap ->
                 fragmentGalleryBinding.imageResult.setImageBitmap(bitmap)

                 // Run face detection on the input image
                 backgroundExecutor.execute {

                     faceDetectorHelper =
                         FaceDetectorHelper(
                             context = baseContext,
                             threshold = viewModel.currentThreshold,
                             currentDelegate = viewModel.currentDelegate,
                             runningMode = RunningMode.IMAGE,
                             faceDetectorListener = this
                         )

                     faceDetectorHelper.detectImage(bitmap)
                         ?.let { resultBundle ->
                             activity?.runOnUiThread {
                                 fragmentGalleryBinding.overlay.setResults(
                                     resultBundle.results[0],
                                     bitmap.height,
                                     bitmap.width
                                 )

                                 setUiEnabled(true)
                                 fragmentGalleryBinding.bottomSheetLayout.inferenceTimeVal.text =
                                     String.format(
                                         "%d ms",
                                         resultBundle.inferenceTime
                                     )
                             }
                         } ?: run {
                         Log.e(TAG, "Error running face detection.")
                     }

                     faceDetectorHelper.clearFaceDetector()
                 }
             }
     }

     // Check the type of media that user selected.
     private fun loadMediaType(uri: Uri): MediaType {
         val mimeType = context?.contentResolver?.getType(uri)
         mimeType?.let {
             if (mimeType.startsWith("image")) return MediaType.IMAGE
             if (mimeType.startsWith("video")) return MediaType.VIDEO
         }

         return MediaType.UNKNOWN
     }

     private fun detectError() {
         activity?.runOnUiThread {
             fragmentGalleryBinding.progress.visibility = View.GONE
             setUiEnabled(true)
             updateDisplayView(MediaType.UNKNOWN)
         }
     }

     override fun onError(error: String, errorCode: Int) {
         detectError()
         activity?.runOnUiThread {
             Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
             if (errorCode == FaceDetectorHelper.GPU_ERROR) {
                 fragmentGalleryBinding.bottomSheetLayout.spinnerDelegate.setSelection(
                     FaceDetectorHelper.DELEGATE_CPU, false
                 )
             }
         }
     }

     override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
         // no-op
     }

     enum class MediaType {
         IMAGE,
         VIDEO,
         UNKNOWN
     }

     */


}