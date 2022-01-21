package com.example.motionpath.ui.create_train

import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.domain.train.Train
import java.util.*

sealed class CreateTrainEvent {
    object onEditDateClicked: CreateTrainEvent()

    object onAddExerciseClicked: CreateTrainEvent()

    object onCollapseOrExpandClicked: CreateTrainEvent()

    data class onSearchClientResultClicked(val client: Client): CreateTrainEvent()

    data class onNameChanged(val name: String): CreateTrainEvent()

    data class onDescriptionChanged(val description: String): CreateTrainEvent()

    data class onGoalChanged(val goal: String): CreateTrainEvent()

    data class onTimeClicked(val type: PickedTime): CreateTrainEvent()

    data class onTimeUpdated(val hour: Int, val minute: Int, val type: PickedTime): CreateTrainEvent()

    data class onDateClicked(val trainDate: TrainDate): CreateTrainEvent()

    data class onDateUpdated(val newDate: Date): CreateTrainEvent()

    data class onRemoveExerciseClicked(val exercise: Exercise): CreateTrainEvent()

    object onSaveClicked: CreateTrainEvent()

    data class onPreviousTrainClicked(val train: Train): CreateTrainEvent()

}