package com.example.motionpath.ui.exercise

import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.model.domain.mock_exercise.MockExerciseType
import kotlinx.coroutines.flow.*

class ExerciseSelectionRepositoryImpl : ExerciseSelectionRepository {
    private val _exercises = MutableStateFlow<List<MockExercise>>(emptyList())

    private val categorySelectionMap = HashMap<Int, Int>()
    private val selectedExercisesMap = HashMap<Int, Int>()

    override fun addExercise(exercise: MockExercise) {
        _exercises.value = _exercises.value.toMutableList().apply {
            add(exercise.copy(viewType = MockExerciseType.ITEM_SELECTED))
        }

        selectedExercisesMap[exercise.id] = selectedExercisesMap[exercise.id]?.let { it + 1 } ?: 1

        exercise.categoryId?.let {
            categorySelectionMap[it] = categorySelectionMap[it]?.let { count -> count + 1 } ?: 1
        }
    }

    override fun removeExercise(exercise: MockExercise) {
        val pos = _exercises.value.indexOfFirst { it.id == exercise.id }
        _exercises.value = _exercises.value.toMutableList().apply {
            removeAt(pos)
        }

        selectedExercisesMap[exercise.id]?.let { count ->
            if (count > 1) {
                selectedExercisesMap[exercise.id] = count - 1
            } else {
                selectedExercisesMap.remove(exercise.id)
            }
        }

        exercise.categoryId?.let { categoryId ->
            categorySelectionMap[categoryId] = categorySelectionMap[categoryId]?.let { count ->
                if (count > 1) {
                    count - 1
                }
                else {
                    0
                }
            } ?: 0
        }
    }

    override fun getExerciseCount(exercise: MockExercise): Int {
        return selectedExercisesMap[exercise.id] ?: 0
    }

    override fun getCategoryExerciseCount(category: MockExercise): Int {
        return categorySelectionMap[category.id] ?: 0
    }

    override fun clear() {
        _exercises.value = emptyList()
        selectedExercisesMap.clear()
        categorySelectionMap.clear()
    }

    override fun getSelectedExercises(): StateFlow<List<MockExercise>> = _exercises
}