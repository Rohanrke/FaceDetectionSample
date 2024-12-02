package com.rohan.face.detection.media.scan.di

import android.content.Context

interface AppComponentProvider {
    fun getPhotoFeedComponent(context: Context): PhotoFeedComponent
}