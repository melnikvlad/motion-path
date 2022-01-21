package com.example.motionpath.data.model.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.motionpath.data.model.entity.ClientEntity
import com.example.motionpath.data.model.entity.TrainEntity

data class ClientWithTrains(
    @Embedded val client: ClientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "clientId"
    )
    val trains: List<TrainEntity>
)
