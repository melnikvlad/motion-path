package com.example.motionpath.ui.create_train

import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.domain.client_category.CategoryType
import java.util.*

data class CreateTrainUiState(
    val mode: Mode = Mode.CREATE,
    val client: Client = Client(),
    val trainDate: TrainDate,
    val exercises: List<Exercise> = emptyList(),
)

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