package com.example.motionpath.model.domain.train

import com.example.motionpath.model.domain.Exercise
import java.util.*

data class Train(
    val id: Int,
    val clientId: Int,
    val timeStart: Date,
    val timeEnd: Date,
    val exercises: List<Exercise> = emptyList(),
    val isSelected: Boolean = false
)
