package com.example.motionpath.ui.create_train

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.motionpath.data.model.entity.TrainEntity
import com.example.motionpath.data.model.entity.toEntity
import com.example.motionpath.domain.ExerciseSelectionRepository
import com.example.motionpath.domain.usecase.client.ClientUseCase
import com.example.motionpath.domain.usecase.common.GetTrainInfoUseCase
import com.example.motionpath.domain.usecase.exercise.ExerciseUseCase
import com.example.motionpath.domain.usecase.train.TrainUseCase
import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.domain.mock_exercise.mapToExercise
import com.example.motionpath.model.domain.train.TrainDateInfoItem
import com.example.motionpath.ui.base.BaseViewModel
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.DEFAULT_ID
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_CLIENT_ID
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_CURRENT_DATE
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_SCREEN_MODE
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_TRAIN_END_DATE
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_TRAIN_ID
import com.example.motionpath.util.extension.executeIO
import com.example.motionpath.util.extension.executeUI
import com.example.motionpath.util.extension.switchUI
import com.example.motionpath.util.newTime
import com.example.motionpath.util.plusHour
import com.example.motionpath.util.validator.ValidationProcesser
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class CreateTrainViewModel @AssistedInject constructor(
    private val trainUseCase: TrainUseCase,
    private val clientUseCase: ClientUseCase,
    private val trainInfoUseCase: GetTrainInfoUseCase,
    private val exerciseUseCase: ExerciseUseCase,
    private val exerciseSelectionRepository: ExerciseSelectionRepository,
    @Assisted args: Bundle
) : BaseViewModel<CreateTrainUiState, CreateTrainAction, CreateTrainEvent>() {

    private val validator by lazy { ValidationProcesser() }

    private val mode = args.getSerializable(KEY_SCREEN_MODE) as Mode
    private val clientId = args.getInt(KEY_CLIENT_ID, DEFAULT_ID)
    private val trainId = args.getInt(KEY_TRAIN_ID, DEFAULT_ID)
    private val date = args.getSerializable(KEY_CURRENT_DATE) as Date
    private var timeStart = args.getSerializable(KEY_TRAIN_END_DATE) as? Date ?: date
    private var timeEnd = args.getSerializable(KEY_TRAIN_END_DATE) as? Date ?: timeStart.plusHour(1)

    init {
        viewState = CreateTrainUiState(
            mode = mode,
            trainDate = TrainDate(date, timeStart, timeEnd),
            isCollapsed = mode == Mode.EDIT
        )

        loadTrainInfo()

        exerciseSelectionRepository.getSelectedExercises()
            .onEach { selectedExercises ->
                val exercises = selectedExercises.mapIndexed { index, mockExercise ->
                    mockExercise.mapToExercise(index)
                }
                viewState = viewState.copy(exercises = exercises)
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        exerciseSelectionRepository.clear()
    }

    override fun processEvent(event: CreateTrainEvent) {
        when (event) {
            CreateTrainEvent.onAddExerciseClicked -> addExercises()

            is CreateTrainEvent.onRemoveExerciseClicked -> removeExercise(event.exercise)

            CreateTrainEvent.onEditDateClicked -> {
                viewAction = CreateTrainAction.OpenDatePickerAction(viewState.trainDate.day)
            }

            is CreateTrainEvent.onDateChanged -> TODO()

            is CreateTrainEvent.onDescriptionChanged -> TODO()
            is CreateTrainEvent.onGoalChanged -> TODO()
            is CreateTrainEvent.onNameChanged -> TODO()
            is CreateTrainEvent.onTimeChanged -> {

            }

            CreateTrainEvent.onCollapseOrExpandClicked -> {
                viewState = viewState.copy(isCollapsed = !viewState.isCollapsed)
            }

            is CreateTrainEvent.onSaveClicked -> {
                applyTrainChanges(event.name, event.description, event.goal)
            }
        }
    }

    private fun loadTrainInfo() {
        viewModelScope.executeIO {

            val (client, exercises) = trainInfoUseCase(this, mode, clientId, trainId)

            preSelectExercises(exercises)

            switchUI {
                viewState = viewState.copy(client = client, exercises = exercises)
            }
        }
    }

    private fun removeExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseSelectionRepository.removeExercise(exercise = exercise.mockExercise)
        }
    }

    private fun addExercises() {
        viewAction = CreateTrainAction.NavigateToSelectExerciseAction
    }

    private fun preSelectExercises(exercises: List<Exercise>) {
        viewModelScope.launch {
            val mockExercises = exercises.map { it.mockExercise }
            if (exerciseSelectionRepository.isEmpty()) {
                exerciseSelectionRepository.addPreSelectedExercises(exercises = mockExercises)
            }
        }
    }

    fun applyTrainChanges(name: String, description: String, goal: String) {
        if (validator.isValid()) {
            viewModelScope.executeIO {
                //TODO: if clientID != null then call update with new name, goal and description
                // otherwise create new client
                val clientId = async { clientUseCase.createClient(name, description, goal) }
                //TODO: if trainId != null then update with new time
                val train = TrainEntity(
                    clientId = clientId.await().toInt(),
                    timeStart = viewState.trainDate.timeStart,
                    timeEnd = viewState.trainDate.timeEnd,
                )
                val trainId = async { trainUseCase.createTrain(train) }

                with(trainId.await()) {
                    val exercises = viewState.exercises.map { exercise ->
                        exercise.copy(trainId = this.toInt()).toEntity()
                    }

                    exerciseUseCase.insertExercises(exercises)

                    switchUI {
                        viewAction = CreateTrainAction.NavigateToTrainsAction
                    }
                }
            }
        }
    }

    fun updateDay(day: Date) {
        viewModelScope.executeUI {
            viewState = viewState.copy(
                trainDate = viewState.trainDate.copy(day = day)
            )
        }
    }

    fun updateTime(hour: Int, minute: Int, pickedTime: PickedTime) {
        when (pickedTime) {
            PickedTime.START -> {
                viewModelScope.executeUI {
                    viewState = viewState.copy(
                        trainDate = viewState.trainDate.copy(
                            timeStart = viewState.trainDate.timeStart.newTime(hour, minute)
                        )
                    )
                }
            }
            PickedTime.END -> {
                viewModelScope.executeUI {
                    viewState = viewState.copy(
                        trainDate = viewState.trainDate.copy(
                            timeEnd = viewState.trainDate.timeEnd.newTime(hour, minute)
                        )
                    )
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
class Factory(
    private val assistedFactory: CreateTrainViewModelAssistedFactory,
    private val args: Bundle
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return assistedFactory.create(args) as T
    }
}