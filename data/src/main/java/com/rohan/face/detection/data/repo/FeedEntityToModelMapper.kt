package com.rohan.face.detection.data.repo

import com.rohan.face.detection.data.entity.FeedEntity
import com.rohan.face.detection.domain.EntityToModelMapper
import com.rohan.face.detection.domain.model.FeedModel
import javax.inject.Inject

class FeedEntityToModelMapper @Inject constructor() : EntityToModelMapper<FeedEntity, FeedModel> {
    override fun map(entity: FeedEntity): FeedModel {
        return FeedModel(
            uri = entity.uri,
            lastUpdatedTimeStamp = entity.lastUpdatedTimeStamp,
            nameTag = entity.nameTag
        )
    }
}