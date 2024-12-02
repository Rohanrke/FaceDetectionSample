package com.rohan.face.detection.media.scan

import android.content.Context
import android.content.Intent
import com.rohan.face.detection.media.scan.kit.MediaScanKit
import com.rohan.face.detection.media.scan.ui.PhotoFeedActivity

class MediaScanKitImpl : MediaScanKit {

    override fun launchPhotoFeed(context: Context) {
        context.startActivity(Intent(context, PhotoFeedActivity::class.java))
    }
}