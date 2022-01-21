package com.example.motionpath.domain.usecase.mock_exercise

import com.example.motionpath.data.model.entity.toDomain
import com.example.motionpath.domain.ExerciseSelectionRepository
import com.example.motionpath.domain.MockExerciseRepository
import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.model.domain.mock_exercise.MockExerciseItemId
import com.example.motionpath.model.domain.mock_exercise.MockExerciseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class GetMockExercicesUseCase(
    private val repository: MockExerciseRepository,
    private val exerciseSelectionRepository: ExerciseSelectionRepository
) {
    operator fun invoke(category: MockExercise): Flow<List<MockExercise>> {
        val selectedExercises = exerciseSelectionRepository.getSelectedExercises()
            .map { list ->
                list
                    .map { it.copy(viewType = MockExerciseType.ITEM_SELECTED) }
                    .groupBy { it.id }
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

            // Add title for selections
            if (selected.isNotEmpty()) {
                result.add(
                    0,
                    MockExercise(
                        id = MockExerciseItemId.ID_TITLE_SELECTED.id,
                        viewType = MockExerciseType.TITLE_SELECTED
                    )
                )

                // Add selected exercises

                selected.keys.forEach { key ->
                    val list = selected[key]
                    list?.let {
                        val item = it[0]
                        item.exerciseSelectedCount = it.size
                        result.add(item)
                    }
                }
            }

            // Add current category name
            result.add(
                MockExercise(
                    id = MockExerciseItemId.ID_TITLE_CATEGORY.id,
                    name = category.name,
                    viewType = MockExerciseType.TITLE_CATEGORY
                )
            )

            val muscleToExercisesMap = current.groupBy { it.muscleName }

            muscleToExercisesMap.keys.forEach {

                // Add category muscle name
                result.add(
                    MockExercise(
                        id = MockExerciseItemId.ID_TITLE_MUSCLE_NAME.id,
                        viewType = MockExerciseType.ITEM_MUSCLE_NAME,
                        muscleName = it
                    )
                )

                // Add category exercises grouped by muscles
                muscleToExercisesMap[it]?.let { muscleExercises ->
                    result.addAll(muscleExercises)
                }
            }

            return@zip result

        }.flowOn(Dispatchers.IO)
    }
}