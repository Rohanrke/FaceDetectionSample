package com.rohan.face.detection.sample.di

import com.rohan.face.detection.media.scan.MediaScanKitImpl
import com.rohan.face.detection.media.scan.kit.MediaScanKit
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    fun provideMediaScanKit(): MediaScanKit = MediaScanKitImpl()
}