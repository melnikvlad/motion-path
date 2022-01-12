package com.example.motionpath.data.model.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.motionpath.data.model.entity.ClientEntity
import com.example.motionpath.data.model.entity.TrainEntity

data class TrainWithClient(
    @Embedded
    val train: TrainEntity,
    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: ClientEntity
)
