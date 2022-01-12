package com.example.motionpath.ui.create_train

sealed class CreateTrainEvent {
    object onEditDateClicked: CreateTrainEvent()

    object onAddExerciseClicked: CreateTrainEvent()

    data class onSaveClicked(
        val name: String,
        val description: String,
        val goal: String
    ): CreateTrainEvent()
}