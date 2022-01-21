package com.example.motionpath.ui.create_train

import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.domain.client_category.CategoryType
import com.example.motionpath.model.domain.train.*
import java.util.*

data class CreateTrainUiState(
    val mode: Mode = Mode.CREATE,
    val client: Client = Client(),
    val clientPreviousTrains: List<Train> = emptyList(),
    val trainDate: TrainDate,
    val exercises: List<Exercise> = emptyList(),
    val isCollapsed: Boolean = false,
    val searchClients: List<Client> = emptyList(),
) {
    fun items(): List<TrainItem> {
        val result = mutableListOf<TrainItem>().apply {
            add(
                TrainInfoItem(
                    ClientInfoItem(client, searchClients),
                    TrainDateInfoItem(trainDate),
                    isCollapsed = isCollapsed
                )
            )

            if (clientPreviousTrains.isNotEmpty()) {
                add(PreviousTrainsItem(clientPreviousTrains))
            }

            add(ExercisesTitleItem)

            addAll(exercises.map { ExerciseItem(it) })
        }

        return result
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