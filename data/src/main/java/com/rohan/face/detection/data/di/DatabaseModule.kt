package com.rohan.face.detection.data.di

import android.content.Context
import com.rohan.face.detection.data.FaceDetectionAppDatabase
import com.rohan.face.detection.data.dao.FeedDao
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    @Provides
    fun provideFeedDao(database: FaceDetectionAppDatabase): FeedDao {
        return database.feedDao()
    }

    @Provides
    fun provideFeedDatabase(context: Context): FaceDetectionAppDatabase {
        return FaceDetectionAppDatabase.getDatabase(context.applicationContext)
    }
}