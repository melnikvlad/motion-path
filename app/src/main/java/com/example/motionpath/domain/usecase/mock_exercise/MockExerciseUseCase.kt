package com.example.motionpath.domain.usecase.mock_exercise


data class MockExerciseUseCase(
    val getCategories: GetMockCategoriesUseCase,
    val getExercices: GetMockExercicesUseCase,
    val searchExercises: GetMockExercisesByQueryUseCase
)