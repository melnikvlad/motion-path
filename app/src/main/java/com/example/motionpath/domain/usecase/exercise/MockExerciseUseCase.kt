package com.example.motionpath.domain.usecase.exercise


data class MockExerciseUseCase(
    val getCategories: GetMockCategoriesUseCase,
    val getExercices: GetMockExercicesUseCase,
    val searchExercises: GetMockExercisesByQueryUseCase
)