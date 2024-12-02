package com.rohan.face.detection.domain

import com.rohan.face.detection.domain.model.FeedModel

interface PhotoFeedRepo {
    suspend fun getAllFeed(): List<FeedModel>
    suspend fun insertFeeds(feedList: List<FeedModel>): List<FeedModel>
    suspend fun updateFeed(feedModel: FeedModel)
}