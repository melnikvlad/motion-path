package com.example.motionpath.domain.usecase.train

data class TrainUseCase(
    val createTrain: CreateTrainUseCase,
    val deleteTrain: DeleteTrainUseCase,
    val getTrainForDate: GetTrainsForDateUseCase,
    val deleteClientTrainsUseCase: DeleteClientTrainsUseCase,
    val getExercises: GetTrainExercisesUseCase
)
