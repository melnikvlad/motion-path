package com.example.motionpath.ui.exercise

import androidx.lifecycle.viewModelScope
import com.example.motionpath.domain.ExerciseSelectionRepository
import com.example.motionpath.domain.usecase.mock_exercise.MockExerciseUseCase
import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.ui.base.BaseViewModel
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
                when(viewState.depth) {
                    SelectionDepth.CATEGORY -> {
                        currentCategory = event.item
                        loadExercises(event.item)
                    }

                    SelectionDepth.EXERCISE -> {
                        viewModelScope.launch {
                            exerciseSelectionRepository.addExercise(event.item)
                        }
                        currentCategory?.let { loadExercises(it) }
                    }

                    SelectionDepth.SEARCH -> {
                        viewModelScope.launch {
                            exerciseSelectionRepository.addExercise(event.item)
                        }
                        // TODO: load exercises of selected exercise category
                    }
                }
            }

            is ExerciseEvent.onItemRemoveClicked -> {
                viewModelScope.launch {
                    exerciseSelectionRepository.removeExercise(event.item)
                }

                when(viewState.depth) {
                    SelectionDepth.CATEGORY -> loadCategories()
                    SelectionDepth.EXERCISE -> currentCategory?.let { loadExercises(it) }
                    SelectionDepth.SEARCH -> Unit
                }
            }

            ExerciseEvent.onBackButtonClicked -> {
                currentCategory = null
                viewState = viewState.copy(depth = SelectionDepth.CATEGORY)
                loadCategories()
            }

            ExerciseEvent.onSearchViewClicked -> {
                viewState = viewState.copy(depth = SelectionDepth.SEARCH)
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            mockExerciseUseCase.getCategories()
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
            mockExerciseUseCase.getExercices(category)
                .collect { list ->
                    viewState = viewState.copy(
                        status = if (list.isNullOrEmpty()) Status.Empty else Status.Data(category),
                        exercise = list,
                        depth = SelectionDepth.EXERCISE
                    )
                }
        }
    }

    private fun searchExercises(query: String) {
        viewModelScope.launch {
            mockExerciseUseCase.searchExercises(query)
                .collect { list ->
                    viewState = viewState.copy(
                        status = Status.Search,
                        exercise = list,
                        depth = SelectionDepth.SEARCH
                    )
                }
        }
    }
}

