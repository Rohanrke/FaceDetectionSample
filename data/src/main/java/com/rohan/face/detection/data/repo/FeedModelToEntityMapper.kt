package com.rohan.face.detection.data.repo

import com.rohan.face.detection.data.entity.FeedEntity
import com.rohan.face.detection.domain.ModelToEntityMapper
import com.rohan.face.detection.domain.model.FeedModel
import javax.inject.Inject

class FeedModelToEntityMapper @Inject constructor() : ModelToEntityMapper<FeedModel, FeedEntity> {

    override fun map(model: FeedModel): FeedEntity {
        return FeedEntity(
            uri = model.uri,
            nameTag = model.nameTag,
            lastUpdatedTimeStamp = System.currentTimeMillis()
        )
    }
}