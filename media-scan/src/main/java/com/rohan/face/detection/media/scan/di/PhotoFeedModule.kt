package com.rohan.face.detection.media.scan.di

import androidx.lifecycle.ViewModel
import com.rohan.face.detection.base.ViewModelKey
import com.rohan.face.detection.data.di.PhotoFeedRepoModule
import com.rohan.face.detection.domain.PhotoFeedRepo
import com.rohan.face.detection.media.scan.ui.PhotoFeedViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [PhotoFeedRepoModule::class])
object PhotoFeedModule {

    // Provides the PhotoFeedViewModel to Dagger using ViewModelKey for multibinding
    @Provides
    @IntoMap
    @ViewModelKey(PhotoFeedViewModel::class)  // This tells Dagger to associate the PhotoFeedViewModel with its key
    fun providePhotoFeedViewModel(
        repo: PhotoFeedRepo
    ): ViewModel {
        return PhotoFeedViewModel(repo)  // Create and return the PhotoFeedViewModel instance
    }
}