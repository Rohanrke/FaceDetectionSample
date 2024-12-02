package com.rohan.face.detection.sample.di

import com.rohan.face.detection.base.ViewModelFactoryModule
import com.rohan.face.detection.media.scan.di.PhotoFeedComponent
import com.rohan.face.detection.sample.FaceDetectionApp
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelFactoryModule::class])  // Include all the necessary modules
interface AppComponent {

    // Method to inject into the Application class if needed
    fun inject(application: FaceDetectionApp)

    // Method to provide the PhotoFeedComponent
    fun photoFeedComponent(): PhotoFeedComponent.Factory
}