package com.example.motionpath.ui.create_train

import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.domain.client_category.CategoryType
import com.example.motionpath.model.domain.train.*
import java.util.*
import kotlin.collections.ArrayList

data class CreateTrainUiState(
    val mode: Mode = Mode.CREATE,
    val client: Client = Client(),
    val trainDate: TrainDate,
    val exercises: List<Exercise> = emptyList(),
    val isCollapsed: Boolean = false
) {
    fun items() = mutableListOf(
        TrainInfoItem(
            ClientInfoItem(client),
            TrainDateInfoItem(trainDate),
            isCollapsed = isCollapsed
        ),
        SelectExerciseItem(),
    ).apply {
        addAll(exercises.map { ExerciseItem(it) })
    }
}

data class Client(
    val id: Int = -1,
    val name: String = "",
    val categoryType: CategoryType = CategoryType.DEFAULT,
    val description: String = "",
    val goal: String = ""
)

data class TrainDate(val day: Date, val timeStart: Date, val timeEnd: Date)

enum class Mode {
    CREATE,
    EDIT
}