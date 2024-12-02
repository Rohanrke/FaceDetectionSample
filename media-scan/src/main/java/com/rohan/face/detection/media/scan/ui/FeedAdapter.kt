package com.rohan.face.detection.media.scan.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohan.face.detection.domain.model.FeedModel
import com.rohan.face.detection.media.scan.FaceDetectorHelper
import com.rohan.face.detection.media.scan.R
import com.rohan.face.detection.media.scan.databinding.ItemFeedBinding
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class FeedAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val onNameTagUpdated: (FeedModel) -> Unit, // Callback for updates
    private val onLongClick: (FeedModel) -> Unit // Callback for face detection
) : ListAdapter<FeedModel, FeedAdapter.FeedViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val viewBinding = ItemFeedBinding.inflate(LayoutInflater.from(context), parent, false)
        return FeedViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val imageModel = getItem(position)

        // Load image using Glide
        Glide.with(context)
            .load(imageModel.uri)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(holder.itemViewBinding.imageView)

        // Set nameTag if available
        imageModel.nameTag?.isNotBlank()?.let {
            holder.itemViewBinding.nameTag.text = imageModel.nameTag
            holder.itemViewBinding.nameTag.visibility = View.VISIBLE
        }

        // Update nameTag on change
        holder.itemViewBinding.root.setOnClickListener {
            onLongClick(imageModel)
        }

       holder.itemViewBinding.root.setOnLongClickListener {
            // open dialog to update
            val dialogFragment = AddNameTagDialogFragment.newInstance(imageModel.uri)
            dialogFragment.setOnTagSaveListener(object :
                AddNameTagDialogFragment.OnTagSaveListener {
                override fun onTagSaved(faceId: Uri, tag: String) {
                    if (tag != imageModel.nameTag) {
                        imageModel.nameTag = tag
                        onNameTagUpdated(imageModel)
                    }
                }
            })
            dialogFragment.show(fragmentManager, AddNameTagDialogFragment.TAG)
            true // Return true to indicate the event is consumed
        }
    }

    // Method to check if the image is currently visible
    private fun RecyclerView.ViewHolder.isImageVisible(): Boolean {
        return itemView.isShown && itemView.bottom > 0 && itemView.top < itemView.context.resources.displayMetrics.heightPixels
    }

    class FeedViewHolder(val itemViewBinding: ItemFeedBinding) : RecyclerView.ViewHolder(itemViewBinding.root)

    internal class ImageDiffCallback : DiffUtil.ItemCallback<FeedModel>() {
        override fun areItemsTheSame(oldItem: FeedModel, newItem: FeedModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FeedModel, newItem: FeedModel): Boolean {
            return oldItem == newItem
        }
    }

}