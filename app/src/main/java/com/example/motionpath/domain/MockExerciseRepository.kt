package com.example.motionpath.domain

import com.example.motionpath.data.model.entity.MockExerciseEntity
import kotlinx.coroutines.flow.Flow

interface MockExerciseRepository {
    fun get(): Flow<List<MockExerciseEntity>>

    fun get(parentId: Int): Flow<List<MockExerciseEntity>>

    fun search(query: String): Flow<List<MockExerciseEntity>>
}