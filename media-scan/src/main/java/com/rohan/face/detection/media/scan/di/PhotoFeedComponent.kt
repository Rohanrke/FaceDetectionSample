package com.rohan.face.detection.media.scan.di

import android.content.Context
import com.rohan.face.detection.base.ActivityScope
import com.rohan.face.detection.base.ViewModelFactoryModule
import com.rohan.face.detection.media.scan.ui.PhotoFeedActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [PhotoFeedModule::class, ViewModelFactoryModule::class])
interface PhotoFeedComponent {

    // Method to inject dependencies into PhotoFeedActivity
    fun inject(photoFeedActivity: PhotoFeedActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): PhotoFeedComponent
    }
}