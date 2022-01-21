package com.example.motionpath.model.domain.train

import com.example.motionpath.data.model.entity.ExerciseEntity
import com.example.motionpath.model.domain.Exercise

data class PreviousTrainsWithExercises(
    val trains: List<Train>
)