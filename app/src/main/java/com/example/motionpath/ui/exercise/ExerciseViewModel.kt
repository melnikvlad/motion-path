package com.example.motionpath.ui.exercise

import androidx.lifecycle.viewModelScope
import com.example.motionpath.domain.usecase.exercise.MockExerciseUseCase
import com.example.motionpath.model.domain.MockExercise
import com.example.motionpath.ui.base.BaseViewModel
import com.example.motionpath.util.extension.executeUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val mockExerciseUseCase: MockExerciseUseCase,
) : BaseViewModel<ExerciseUiState, ExerciseAction, ExerciseEvent>() {

    init {
        viewState = ExerciseUiState(status = Status.Loading)
        loadCategories()
    }

    override fun processEvent(event: ExerciseEvent) {
        when (event) {
            is ExerciseEvent.onItemClicked -> {
                if (viewState.depth == SelectionDepth.CATEGORY) {
                    loadExercises(event.item.id)
                } else {
                    updateSelection(event.item)
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.executeUI {
            mockExerciseUseCase.getMockExercises()
                .collect { list ->
                    viewState = viewState.copy(
                        categories = list,
                        status = if (list.isNullOrEmpty()) Status.Empty else Status.Data
                    )
                }
        }
    }

    private fun loadExercises(id: Int) {
        viewModelScope.executeUI {
            mockExerciseUseCase.getImpicitMockExercices(id)
                .collect { list ->
                    viewState = viewState.copy(
                        exercise = list,
                        status = if (list.isNullOrEmpty()) Status.Empty else Status.Data,
                        depth = if (viewState.depth == SelectionDepth.CATEGORY) SelectionDepth.EXERCISE else SelectionDepth.CATEGORY
                    )
                }
        }
    }

    private fun updateSelection(exercise: MockExercise) {
        val exercisePos = viewState.exercise.indexOf(exercise)
        val newExercise = exercise.copy(isSelected = !exercise.isSelected)

        val newExerciseList = viewState.exercise.toMutableList().apply {
            remove(exercise)
            add(exercisePos, newExercise)
        }

        val category = viewState.categories.first { it.id == newExercise.categoryId }
        val categoryPos = viewState.categories.indexOf(category)

        val newSelectedExerciseCount = if (newExercise.isSelected)
            category.exerciseSelectedCount + 1
        else
            category.exerciseSelectedCount - 1

        val newCategory = category.copy(exerciseSelectedCount = newSelectedExerciseCount)

        val newCategoriesList = viewState.categories.toMutableList().apply {
            remove(category)
            add(categoryPos, newCategory)
        }

        viewState = viewState.copy(
            categories = newCategoriesList,
            exercise = newExerciseList
        )
    }
}

