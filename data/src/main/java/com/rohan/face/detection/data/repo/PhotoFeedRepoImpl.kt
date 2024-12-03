package com.rohan.face.detection.data.repo

import com.rohan.face.detection.data.dao.FeedDao
import com.rohan.face.detection.data.entity.FeedEntity
import com.rohan.face.detection.domain.EntityToModelMapper
import com.rohan.face.detection.domain.ModelToEntityMapper
import com.rohan.face.detection.domain.PhotoFeedRepo
import com.rohan.face.detection.domain.model.FeedModel

class PhotoFeedRepoImpl(
    private val dao: FeedDao,
    private val entityMapper: ModelToEntityMapper<FeedModel, FeedEntity>,
    private val modelMapper: EntityToModelMapper<FeedEntity, FeedModel>
) : PhotoFeedRepo {

    override suspend fun getAllFeed(): List<FeedModel> {
        val list = mutableListOf<FeedModel>()
        dao.getAllImages().forEach {
            val model = modelMapper.map(it)
            list.add(model)
        }
        return list
    }

    override suspend fun insertFeeds(feedList: List<FeedModel>): List<FeedModel> {
        val list = mutableListOf<FeedEntity>()
        feedList.forEach {
            val entity = entityMapper.map(it)
            list.add(entity)
        }
        dao.insertImages(list)
        return getAllFeed()
    }

    override suspend fun updateFeed(feedModel: FeedModel) {
        val entity = entityMapper.map(feedModel)
        dao.updateImage(entity)
    }
}