package com.rohan.face.detection.data.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rohan.face.detection.data.entity.FeedEntity

@Dao
interface FeedDao {

    @Query("SELECT * FROM feed_table ORDER BY last_updated_time_stamp DESC")
    fun getAllImages(): List<FeedEntity>

    @Query("SELECT COUNT(*) FROM feed_table")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(images: List<FeedEntity>): List<Long>

    @Update
    fun updateImage(image: FeedEntity): Int

    @Delete
    fun deleteImage(image: FeedEntity): Int
}