package com.example.motionpath.data.model.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.motionpath.data.model.entity.ExerciseEntity
import com.example.motionpath.data.model.entity.TrainEntity
import com.example.motionpath.data.model.entity.toDomain
import com.example.motionpath.model.domain.train.PreviousTrainsWithExercises
import com.example.motionpath.model.domain.train.Train

data class TrainWithExercises(
    @Embedded
    val train: TrainEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trainId"
    )
    val exercises: List<ExerciseEntity>
)

fun TrainWithExercises.toDomain(): Train {
    return Train(
        train.id ?: -1,
        train.clientId,
        train.timeStart,
        train.timeEnd,
        exercises.mapIndexed { index, exerciseEntity -> exerciseEntity.toDomain(index) }
    )
}