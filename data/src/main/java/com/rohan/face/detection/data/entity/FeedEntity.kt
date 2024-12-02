package com.rohan.face.detection.data.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_table")
data class FeedEntity(
    @PrimaryKey
    @ColumnInfo(name = "image_uri") val uri: Uri,
    @ColumnInfo(name = "last_updated_time_stamp") val lastUpdatedTimeStamp: Long,
    @ColumnInfo(name = "name_tag")  var nameTag: String? = ""
)
