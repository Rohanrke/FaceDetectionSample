package com.rohan.face.detection.sample

import android.app.Application
import android.content.Context
import com.rohan.face.detection.media.scan.di.AppComponentProvider
import com.rohan.face.detection.media.scan.di.PhotoFeedComponent
import com.rohan.face.detection.sample.di.AppComponent
import com.rohan.face.detection.sample.di.DaggerAppComponent

class FaceDetectionApp: Application(), AppComponentProvider {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // Initialize the AppComponent
        appComponent = DaggerAppComponent.builder()
            .build()
        appComponent.inject(this)
    }

    override fun getPhotoFeedComponent(context: Context): PhotoFeedComponent {
        return appComponent.photoFeedComponent().create(context)
    }
}