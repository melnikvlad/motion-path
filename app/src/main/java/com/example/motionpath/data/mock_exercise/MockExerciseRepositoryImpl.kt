package com.example.motionpath.data.mock_exercise

import com.example.motionpath.data.db.MockExerciseDao
import com.example.motionpath.data.model.entity.MockExerciseEntity
import com.example.motionpath.domain.MockExerciseRepository
import kotlinx.coroutines.flow.Flow

class MockExerciseRepositoryImpl(private val mockExerciseDao: MockExerciseDao):
    MockExerciseRepository {

    override fun get(): Flow<List<MockExerciseEntity>> {
        return mockExerciseDao.get()
    }

    override fun get(parentId: Int): Flow<List<MockExerciseEntity>> {
        return mockExerciseDao.get(parentId)
    }

    override fun search(query: String): Flow<List<MockExerciseEntity>> {
        return mockExerciseDao.search(query)
    }
}