package com.rohan.face.detection.domain

interface EntityToModelMapper<E,M>{
    fun map(entity: E): M
}

interface ModelToEntityMapper<M,E>{
    fun map(model: M): E
}