package com.example.motionpath.domain.usecase.exercise

import com.example.motionpath.domain.usecase.MockExerciseRepository
import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.model.domain.mock_exercise.MockExerciseItemId
import com.example.motionpath.model.domain.mock_exercise.MockExerciseType
import com.example.motionpath.model.entity.toDomain
import com.example.motionpath.ui.exercise.ExerciseSelectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class GetMockExercicesUseCase(
    private val repository: MockExerciseRepository,
    private val exerciseSelectionRepository: ExerciseSelectionRepository
) {
    operator fun invoke(category: MockExercise): Flow<List<MockExercise>> {
        val selectedExercises = exerciseSelectionRepository.getSelectedExercises()
            .map { list ->
                list.map { it.copy(viewType = MockExerciseType.ITEM_SELECTED) }
            }

        val categoryExercises = repository.get(category.id)
            .map { list ->
                list.map {
                    val temp = it.toDomain()
                    temp.exerciseSelectedCount = exerciseSelectionRepository.getExerciseCount(temp)
                    temp
                }
            }

        return selectedExercises.zip(categoryExercises) { selected, current ->
            val result = ArrayList<MockExercise>()

            if (selected.isNotEmpty()) {
                result.add(
                    0,
                    MockExercise(
                        id = MockExerciseItemId.TITLE_SELECTED.id,
                        viewType = MockExerciseType.TITLE_SELECTED
                    )
                )
                result.addAll(selected)
            }

            result.add(
                MockExercise(
                    id = MockExerciseItemId.TITLE_CATEGORY.id,
                    name = category.name,
                    viewType = MockExerciseType.TITLE_CATEGORY
                )
            )

            result.addAll(current)

            return@zip result

        }.flowOn(Dispatchers.IO)
    }
}