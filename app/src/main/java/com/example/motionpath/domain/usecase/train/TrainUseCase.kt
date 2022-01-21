package com.example.motionpath.domain.usecase.train

import com.example.motionpath.domain.usecase.client.GetClientPreviousTrains

data class TrainUseCase(
    val createTrain: CreateTrainUseCase,
    val deleteTrain: DeleteTrainUseCase,
    val getTrainForDate: GetTrainsForDateUseCase,
    val deleteClientTrainsUseCase: DeleteClientTrainsUseCase,
    val getExercises: GetTrainExercisesUseCase,
    val getPreviousTrains: GetClientPreviousTrains
)
