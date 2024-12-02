package com.rohan.face.detection.media.scan.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rohan.face.detection.media.scan.R
import com.rohan.face.detection.media.scan.databinding.DialogAddNameTagBinding

class AddNameTagDialogFragment: DialogFragment() {

    private var listener: OnTagSaveListener? = null

    interface OnTagSaveListener {
        fun onTagSaved(faceId: Uri, tag: String)
    }

    private lateinit var faceId: Uri

    // Factory method to create an instance of the dialog with the faceId
    companion object {
        const val TAG = "AddNameTagDialogFragment"
        fun newInstance(faceId: Uri): AddNameTagDialogFragment {
            val fragment = AddNameTagDialogFragment()
            fragment.arguments =  Bundle().apply {
                putParcelable("FACE_ID", faceId)
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate your custom view for the dialog
        val binding = DialogAddNameTagBinding.inflate(inflater, container, false)

        // Retrieve faceId from arguments
        faceId = arguments?.getParcelable<Uri>("FACE_ID") ?: Uri.EMPTY

        // Set up the dialog's actions
        binding.btnSave.setOnClickListener {
            val tag = binding.editTextTag.text.toString()
            listener?.onTagSaved(faceId, tag)
            dismiss()  // Close the dialog
        }

        binding.btnCancel.setOnClickListener {
            dismiss()  // Close the dialog
        }

        return binding.root
    }

    fun setOnTagSaveListener(listener: OnTagSaveListener){
        this.listener = listener
    }

    override fun onStart() {
        super.onStart()

        // Adjust the size of the dialog to look like a popup
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(), // 80% of screen width
            ViewGroup.LayoutParams.WRAP_CONTENT // Height wraps content
        )

        // Optional: Add popup-style animations
        dialog?.window?.setWindowAnimations(R.style.DialogPopupAnimation)
    }

}