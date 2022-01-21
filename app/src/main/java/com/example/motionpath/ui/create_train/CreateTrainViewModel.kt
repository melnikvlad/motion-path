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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
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

    private var tempClientInfo: Client = Client()

    private var tempSelectedExercise: List<Exercise> = emptyList()

    private var searchClientsByQueryJob: Job? = null

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
                tempSelectedExercise = exercises
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

            is CreateTrainEvent.onDateClicked -> {
                viewAction = CreateTrainAction.OpenDatePickerAction(event.trainDate.day)
            }

            is CreateTrainEvent.onDateUpdated -> updateDay(event.newDate)

            is CreateTrainEvent.onTimeClicked -> {
                viewAction = when (event.type) {
                    PickedTime.START -> CreateTrainAction.OpenTimeStartPickerAction
                    PickedTime.END -> CreateTrainAction.OpenTimeEndPickerAction
                }
            }

            is CreateTrainEvent.onTimeUpdated -> updateTime(event.hour, event.minute, event.type)

            is CreateTrainEvent.onDescriptionChanged -> {
                //tempClientInfo = tempClientInfo.copy(description = event.description)
            }
            is CreateTrainEvent.onGoalChanged -> {
                //tempClientInfo = tempClientInfo.copy(goal = event.goal)
            }
            is CreateTrainEvent.onNameChanged -> {
                searchClientsByQueryJob?.cancel()
                tempClientInfo = tempClientInfo.copy(name = event.name)

                searchClientsByQueryJob = viewModelScope.launch {
                    clientUseCase.searchClients(event.name)
                        .collect { clients ->
                            viewState = viewState.copy(searchClients = clients)
                        }
                }
            }

            is CreateTrainEvent.onSearchClientResultClicked -> {
                viewModelScope.launch {
                    tempClientInfo = event.client
                    trainUseCase.getPreviousTrains(event.client.id, timeEnd.time)
                        .collect { trains ->
                            viewState = viewState.copy(
                                client = event.client,
                                searchClients = emptyList(),
                                clientPreviousTrains = trains
                            )
                        }
                }
            }

            CreateTrainEvent.onCollapseOrExpandClicked -> {
                viewState = viewState.copy(
                    isCollapsed = !viewState.isCollapsed,
                    client = tempClientInfo
                )

                if (!viewState.isCollapsed) {
                    viewAction = CreateTrainAction.ScrollToStart
                }
            }

            CreateTrainEvent.onSaveClicked -> saveTrain()

            is CreateTrainEvent.onPreviousTrainClicked -> {

                viewModelScope.launch {
                    try {
                        preSelectExercises(event.train.exercises)
                    } catch (e: Exception) {
                        return@launch
                    }
                }

                val clientPreviousTrains = viewState.clientPreviousTrains
                    .map {
                        when {
                            it.id == event.train.id -> {
                                it.copy(isSelected = !it.isSelected)
                            }
                            it.isSelected -> {
                                it.copy(isSelected = false)
                            }
                            else -> {
                                it
                            }
                        }
                    }

                viewState = viewState.copy(
                    exercises = event.train.exercises,
                    clientPreviousTrains = clientPreviousTrains
                )
            }

            CreateTrainEvent.onEditDateClicked -> {
                //TODO
            }
        }
    }

    private fun loadTrainInfo() {
        viewModelScope.executeIO {

            val (client, exercises) = trainInfoUseCase(this, mode, clientId, trainId)

            preSelectExercises(exercises)

            tempClientInfo = client

            trainUseCase.getPreviousTrains(client.id, timeEnd.time)
                .collect { trains ->
                    viewState = viewState.copy(
                        client = client,
                        exercises = exercises,
                        clientPreviousTrains = trains
                    )
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

    private suspend fun preSelectExercises(exercises: List<Exercise>) {
        val mockExercises = exercises.map { it.mockExercise }
        if (exerciseSelectionRepository.isEmpty()) {
            exerciseSelectionRepository.addPreSelectedExercises(exercises = mockExercises)
        }
    }

    fun saveTrain() {
        if (validator.isValid()) {
            viewModelScope.executeIO {
                //TODO: if clientID != null then call update with new name, goal and description
                // otherwise create new client
                val insertClientJob = async {
                    clientUseCase.createClient(tempClientInfo)
                }
                //TODO: if trainId != null then update with new time

                val clientId: Int = if (viewState.client.id != -1)
                    viewState.client.id
                else
                    insertClientJob.await().toInt()

                val train = TrainEntity(
                    clientId = clientId,
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

    private fun updateDay(day: Date) {
        viewModelScope.executeUI {
            viewState = viewState.copy(
                trainDate = viewState.trainDate.copy(day = day)
            )
        }
    }

    private fun updateTime(hour: Int, minute: Int, pickedTime: PickedTime) {
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