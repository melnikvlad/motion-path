package com.example.motionpath.model.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.motionpath.model.entity.ClientEntity
import com.example.motionpath.model.entity.TrainEntity

data class TrainWithClient(
    @Embedded
    val train: TrainEntity,
    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: ClientEntity
)
