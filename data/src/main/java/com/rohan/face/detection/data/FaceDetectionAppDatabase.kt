package com.rohan.face.detection.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rohan.face.detection.data.dao.FeedDao
import com.rohan.face.detection.data.entity.FeedEntity

@Database(entities = [FeedEntity::class], version = 1, exportSchema = false)
@TypeConverters(UriConverter::class) // Register the converter here
abstract class FaceDetectionAppDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao

    companion object {
        @Volatile
        private var INSTANCE: FaceDetectionAppDatabase? = null

        fun getDatabase(context: Context): FaceDetectionAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FaceDetectionAppDatabase::class.java,
                    "face_detection_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}