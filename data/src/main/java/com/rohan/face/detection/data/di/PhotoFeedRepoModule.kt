package com.rohan.face.detection.data.di

import com.rohan.face.detection.base.ActivityScope
import com.rohan.face.detection.data.dao.FeedDao
import com.rohan.face.detection.data.entity.FeedEntity
import com.rohan.face.detection.data.repo.FeedEntityToModelMapper
import com.rohan.face.detection.data.repo.FeedModelToEntityMapper
import com.rohan.face.detection.data.repo.PhotoFeedRepoImpl
import com.rohan.face.detection.domain.EntityToModelMapper
import com.rohan.face.detection.domain.ModelToEntityMapper
import com.rohan.face.detection.domain.PhotoFeedRepo
import com.rohan.face.detection.domain.model.FeedModel
import dagger.Module
import dagger.Provides

@Module(includes = [DatabaseModule::class])
class PhotoFeedRepoModule {

    @ActivityScope
    @Provides
    fun provideFeedModelToEntityMapper(): ModelToEntityMapper<FeedModel, FeedEntity> {
        return FeedModelToEntityMapper()
    }

    @ActivityScope
    @Provides
    fun provideFeedEntityToModelMapper(): EntityToModelMapper<FeedEntity, FeedModel> {
        return FeedEntityToModelMapper()
    }

    @ActivityScope
    @Provides
    fun providePhotoFeedRepository(
        feedDao: FeedDao,
        entityMapper: ModelToEntityMapper<FeedModel, FeedEntity>,
        modelMapper: EntityToModelMapper<FeedEntity, FeedModel>
    ): PhotoFeedRepo {
        return PhotoFeedRepoImpl(feedDao, entityMapper, modelMapper)
    }
}