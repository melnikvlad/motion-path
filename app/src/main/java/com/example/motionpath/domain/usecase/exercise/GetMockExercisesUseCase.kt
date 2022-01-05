package com.example.motionpath.domain.usecase.exercise

import com.example.motionpath.domain.usecase.MockExerciseRepository
import com.example.motionpath.model.domain.MockExercise
import com.example.motionpath.model.entity.MockExerciseEntity
import com.example.motionpath.model.entity.toDomain
import com.example.motionpath.util.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetMockExercisesUseCase(
    private val repository: MockExerciseRepository) {

    operator fun invoke(): Flow<List<MockExercise>> {
        return repository.get()
            .flowOn(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }
            .flowOn(Dispatchers.Default)
    }
}