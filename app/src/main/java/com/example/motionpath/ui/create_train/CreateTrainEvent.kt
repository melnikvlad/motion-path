package com.example.motionpath.ui.create_train

import com.example.motionpath.model.domain.Exercise

sealed class CreateTrainEvent {
    object onEditDateClicked: CreateTrainEvent()

    object onAddExerciseClicked: CreateTrainEvent()

    object onCollapseOrExpandClicked: CreateTrainEvent()

    data class onNameChanged(val client: Client): CreateTrainEvent()

    data class onDescriptionChanged(val client: Client): CreateTrainEvent()

    data class onGoalChanged(val client: Client): CreateTrainEvent()

    data class onTimeChanged(val trainDate: TrainDate, val type: PickedTime): CreateTrainEvent()

    data class onDateChanged(val trainDate: TrainDate): CreateTrainEvent()

    data class onRemoveExerciseClicked(val exercise: Exercise): CreateTrainEvent()

    data class onSaveClicked(val name: String, val description: String, val goal: String): CreateTrainEvent()
}