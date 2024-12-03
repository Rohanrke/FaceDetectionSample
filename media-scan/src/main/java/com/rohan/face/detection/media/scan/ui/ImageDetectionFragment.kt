package com.rohan.face.detection.media.scan.ui

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.rohan.face.detection.media.scan.FaceDetectorHelper
import com.rohan.face.detection.media.scan.databinding.FragmentImageDetectionBinding
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class ImageDetectionFragment: Fragment(), FaceDetectorHelper.DetectorListener  {
    private var _delegate: Int = FaceDetectorHelper.DELEGATE_CPU
    private var _threshold: Float =
        FaceDetectorHelper.THRESHOLD_DEFAULT
    enum class MediaType {
        IMAGE,
        VIDEO,
        UNKNOWN
    }

    private lateinit var fragmentBinding: FragmentImageDetectionBinding

    private lateinit var faceDetectorHelper: FaceDetectorHelper

    /** Blocking ML operations are performed using this executor */
    private lateinit var backgroundExecutor: ScheduledExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentImageDetectionBinding.inflate(inflater, container, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Uri>("FACE_ID")?.let {
            runDetectionOnImage(it)
        }
    }

    override fun onPause() {
        fragmentBinding.overlay.clear()
        super.onPause()
    }

    // Load and display the image.
    private fun runDetectionOnImage(uri: Uri) {
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(
                requireActivity().contentResolver,
                uri
            )
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                uri
            )
        }
            .copy(Bitmap.Config.ARGB_8888, true)
            ?.let { bitmap ->
                fragmentBinding.imageResult.setImageBitmap(bitmap)

                // Run face detection on the input image
                backgroundExecutor.execute {

                    faceDetectorHelper =
                        FaceDetectorHelper(
                            context = requireContext(),
                            threshold = _threshold,
                            currentDelegate = _delegate,
                            runningMode = RunningMode.IMAGE,
                            faceDetectorListener = this
                        )

                    faceDetectorHelper.detectImage(bitmap)
                        ?.let { resultBundle ->
                            activity?.runOnUiThread {
                                fragmentBinding.overlay.setResults(
                                    resultBundle.results[0],
                                    bitmap.height,
                                    bitmap.width
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
            fragmentBinding.progress.visibility = View.GONE
        }
    }

    override fun onError(error: String, errorCode: Int) {
        detectError()
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            if (errorCode == FaceDetectorHelper.GPU_ERROR) {

            }
        }
    }

    override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
        // no-op
    }

    companion object {
        private const val TAG = "ImageDetectionFragment"
        fun newInstance(faceId: Uri): ImageDetectionFragment {
            val fragment = ImageDetectionFragment()
            fragment.arguments =  Bundle().apply {
                putParcelable("FACE_ID", faceId)
            }
            return fragment
        }
    }
}