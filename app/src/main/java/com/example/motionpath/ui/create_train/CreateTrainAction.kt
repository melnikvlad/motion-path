package com.example.motionpath.ui.create_train

import java.util.*

sealed class CreateTrainAction {

    object NavigateToSelectExerciseAction: CreateTrainAction()

    object NavigateToTrainsAction: CreateTrainAction()

    data class OpenDatePickerAction(val day: Date): CreateTrainAction()
}