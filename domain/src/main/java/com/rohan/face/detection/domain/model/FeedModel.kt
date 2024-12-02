package com.rohan.face.detection.domain.model

import android.net.Uri
data class FeedModel(
    val uri: Uri,
    val lastUpdatedTimeStamp: Long,
    var nameTag: String? = ""
)
