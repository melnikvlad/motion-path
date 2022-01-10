package com.example.motionpath.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.mock_exercise.MockExercise

@Entity(tableName = "mock_exercises")
data class MockExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val parentId: Int? = null,
    val name: String,
    //TODO: weight, tries, killos etc.
)

fun MockExerciseEntity.toDomain() = MockExercise(
    id = id ?: -1,
    categoryId = parentId,
    name = name
)

fun MockExercise.toEntity() = MockExerciseEntity(
    id = id,
    parentId = categoryId,
    name = name
)
