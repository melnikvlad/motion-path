package com.example.motionpath.domain.usecase

import com.example.motionpath.model.entity.MockExerciseEntity
import kotlinx.coroutines.flow.Flow

interface MockExerciseRepository {
    fun get(): Flow<List<MockExerciseEntity>>

    fun get(parentId: Int): Flow<List<MockExerciseEntity>>
}