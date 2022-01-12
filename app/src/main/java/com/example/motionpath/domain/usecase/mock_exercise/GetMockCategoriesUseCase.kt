package com.example.motionpath.domain.usecase.mock_exercise

import com.example.motionpath.data.model.entity.toDomain
import com.example.motionpath.domain.MockExerciseRepository
import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.model.domain.mock_exercise.MockExerciseItemId
import com.example.motionpath.model.domain.mock_exercise.MockExerciseType
import com.example.motionpath.domain.ExerciseSelectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

class GetMockCategoriesUseCase(
    private val repository: MockExerciseRepository,
    private val exerciseSelectionRepository: ExerciseSelectionRepository
) {

    operator fun invoke(): Flow<List<MockExercise>> {
        val selectedExercises = exerciseSelectionRepository.getSelectedExercises()

        val categoryExercises = repository.get()
            .map { list ->
                list.map {
                    val temp = it.toDomain()
                    temp.exerciseSelectedCount = exerciseSelectionRepository.getCategoryExerciseCount(temp)
                    temp
                }
            }

        return selectedExercises.zip(categoryExercises) { selected, current ->
            val result = ArrayList<MockExercise>()

            if (selected.isNotEmpty()) {
                result.add(
                    0,
                    MockExercise(
                        id = MockExerciseItemId.ID_TITLE_SELECTED.id,
                        viewType = MockExerciseType.TITLE_SELECTED
                    )
                )

                result.addAll(selected)
            }

            result.add(
                MockExercise(
                    id = MockExerciseItemId.ID_TITLE_CATEGORY.id,
                    viewType = MockExerciseType.TITLE_CATEGORY
                )
            )

            result.addAll(current)

            return@zip result

        }.flowOn(Dispatchers.IO)
    }
}