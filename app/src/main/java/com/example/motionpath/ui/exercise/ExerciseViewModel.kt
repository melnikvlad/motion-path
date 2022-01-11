package com.example.motionpath.ui.exercise

import androidx.lifecycle.viewModelScope
import com.example.motionpath.domain.usecase.exercise.MockExerciseUseCase
import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.model.domain.mock_exercise.MockExerciseItemId
import com.example.motionpath.model.domain.mock_exercise.MockExerciseType
import com.example.motionpath.ui.base.BaseViewModel
import com.example.motionpath.util.extension.executeIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val mockExerciseUseCase: MockExerciseUseCase,
    private val exerciseSelectionRepository: ExerciseSelectionRepository
) : BaseViewModel<ExerciseUiState, ExerciseAction, ExerciseEvent>() {

    init {
        viewState = ExerciseUiState(status = Status.Loading)
        loadCategories()
    }

    private var currentCategory: MockExercise? = null

    override fun processEvent(event: ExerciseEvent) {
        when (event) {
            is ExerciseEvent.onItemClicked -> {
                if (viewState.depth == SelectionDepth.CATEGORY) {
                    currentCategory = event.item
                    loadExercises(event.item)
                } else {
                    exerciseSelectionRepository.addExercise(event.item)
                    currentCategory?.let { loadExercises(it) }
                }
            }

            is ExerciseEvent.onItemRemoveClicked -> {
                exerciseSelectionRepository.removeExercise(event.item)
                when(viewState.depth) {
                    SelectionDepth.CATEGORY -> {
                        loadCategories()
                    }
                    SelectionDepth.EXERCISE -> {
                        currentCategory?.let { loadExercises(it) }
                    }
                }
            }

            ExerciseEvent.onBackButtonClicked -> {
                currentCategory = null
                viewState = viewState.copy(depth = SelectionDepth.CATEGORY)
                loadCategories()
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            mockExerciseUseCase.getMockCategories()
                .collect { list ->
                    viewState = viewState.copy(
                        categories = list,
                        status = if (list.isNullOrEmpty()) Status.Empty else Status.Data(),
                    )
                }
        }
    }

    private fun loadExercises(category: MockExercise) {
        viewModelScope.launch {
            mockExerciseUseCase.getMockExercices(category)
                .collect { list ->
                    viewState = viewState.copy(
                        status = if (list.isNullOrEmpty()) Status.Empty else Status.Data(category),
                        exercise = list,
                        depth = SelectionDepth.EXERCISE
                    )
                }
        }
    }
}

