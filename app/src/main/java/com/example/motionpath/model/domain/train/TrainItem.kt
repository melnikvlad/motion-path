package com.example.motionpath.model.domain.train

import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.ui.create_train.Client
import com.example.motionpath.ui.create_train.TrainDate
import java.util.*

open class TrainItem constructor(val viewType: ViewType) {

    companion object ItemId {
        const val CLIENT_INFO = 1
        const val EXERCISE = 2
        const val SELECT_EXERCISE = 3
        const val TRAIN_DATE_INFO = 4
        const val TRAIN_INFO_ITEM = 5
    }

    class ViewType constructor(val code: Int)

    override fun equals(other: Any?): Boolean {
        return other is TrainItem
                && other.viewType == viewType
                && other == this
    }

    override fun hashCode() = Objects.hash(this)
}

data class TrainInfoItem(
    val client: ClientInfoItem,
    val trainDate: TrainDateInfoItem,
    val isCollapsed: Boolean = false
): TrainItem(viewType = ViewType(TRAIN_INFO_ITEM))

data class ClientInfoItem(
    val client: Client = Client()
) : TrainItem(viewType = ViewType(CLIENT_INFO))

data class TrainDateInfoItem(
    val trainDate: TrainDate
) : TrainItem(viewType = ViewType(TRAIN_DATE_INFO))

data class ExerciseItem(val exercise: Exercise) : TrainItem(viewType = ViewType(EXERCISE))

class SelectExerciseItem : TrainItem(viewType = ViewType(SELECT_EXERCISE))
