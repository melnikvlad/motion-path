package com.example.motionpath.model.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.motionpath.model.entity.ExerciseEntity
import com.example.motionpath.model.entity.TrainEntity

data class TrainWithExercises(
    @Embedded
    val train: TrainEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trainId"
    )
    val exercises: List<ExerciseEntity>
)