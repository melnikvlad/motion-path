package com.example.motionpath.domain.usecase.mock_exercise

import com.example.motionpath.data.model.entity.toDomain
import com.example.motionpath.domain.ExerciseSelectionRepository
import com.example.motionpath.domain.MockExerciseRepository
import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.model.domain.mock_exercise.MockExerciseItemId
import com.example.motionpath.model.domain.mock_exercise.MockExerciseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetMockExercisesByQueryUseCase(
    private val repository: MockExerciseRepository,
    private val exerciseSelectionRepository: ExerciseSelectionRepository
) {
    operator fun invoke(query: String): Flow<List<MockExercise>> {
        return repository.search(query)
            .map { list ->
                list.map {
                    val temp = it.toDomain()
                    temp.exerciseSelectedCount = exerciseSelectionRepository.getExerciseCount(temp)
                    temp
                }
            }
            .map { current ->

                val result = ArrayList<MockExercise>()

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

                return@map result

            }.flowOn(Dispatchers.IO)
    }
}