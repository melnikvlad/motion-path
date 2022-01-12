package com.example.motionpath.data.model.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.motionpath.data.model.entity.ExerciseEntity
import com.example.motionpath.data.model.entity.TrainEntity

data class TrainWithExercises(
    @Embedded
    val train: TrainEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trainId"
    )
    val exercises: List<ExerciseEntity>
)