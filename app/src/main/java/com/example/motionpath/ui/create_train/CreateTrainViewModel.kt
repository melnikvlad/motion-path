package com.example.motionpath.ui.create_train

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.motionpath.data.model.entity.TrainEntity
import com.example.motionpath.data.model.entity.toEntity
import com.example.motionpath.domain.usecase.client.ClientUseCase
import com.example.motionpath.domain.usecase.train.TrainUseCase
import com.example.motionpath.model.domain.client.Client
import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.DEFAULT_ID
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_CLIENT_ID
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_CURRENT_DATE
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_SCREEN_MODE
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_TRAIN_END_DATE
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.KEY_TRAIN_ID
import com.example.motionpath.domain.ExerciseSelectionRepository
import com.example.motionpath.domain.usecase.exercise.ExerciseUseCase
import com.example.motionpath.model.domain.mock_exercise.mapToExercise
import com.example.motionpath.util.extension.executeIO
import com.example.motionpath.util.extension.executeUI
import com.example.motionpath.util.newTime
import com.example.motionpath.util.plusHour
import com.example.motionpath.util.validator.ValidationProcesser
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class CreateTrainViewModel @AssistedInject constructor(
    private val trainUseCase: TrainUseCase,
    private val clientUseCase: ClientUseCase,
    private val exerciseUseCase: ExerciseUseCase,
    private val exerciseSelectionRepository: ExerciseSelectionRepository,
    @Assisted args: Bundle
) : ViewModel() {

    enum class Mode {
        CREATE,
        EDIT
    }

    private val mode = args.getSerializable(KEY_SCREEN_MODE) as Mode
    private val clientId = args.getInt(KEY_CLIENT_ID, DEFAULT_ID)
    private val trainId = args.getInt(KEY_TRAIN_ID, DEFAULT_ID)
    private val date = args.getSerializable(KEY_CURRENT_DATE) as Date
    private var timeStart = date
    private var timeEnd = args.getSerializable(KEY_TRAIN_END_DATE) as? Date
        ?: timeStart.plusHour(1)

    private val _state = MutableStateFlow(
        State(
            client = Client(),
            exercises = emptyList(),
            date = DateState(date, timeStart, timeEnd)
        )
    )
    val state = _state.asLiveData()

    private val validator by lazy { ValidationProcesser() }

    init {
        getClient()
        getExercises()

        exerciseSelectionRepository.getSelectedExercises()
            .onEach { selectedExercises ->
                val exercises = selectedExercises.mapIndexed { index, mockExercise ->
                    mockExercise.mapToExercise(index)
                }
                _state.emit(_state.value.copy(exercises = exercises))
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        exerciseSelectionRepository.clear()
    }

    private fun getClient() {
        viewModelScope.executeIO {
            val client = clientUseCase.getClient(clientId)
            when {
                client != null -> _state.emit(_state.value.copy(client = client))
                else -> { /* TODO error or not ? */  }
            }
        }
    }

    private fun getExercises() {
        if (mode == Mode.EDIT) {
            viewModelScope.executeIO {
                val exercises = trainUseCase.getExercises(trainId)
                _state.emit(_state.value.copy(exercises = exercises))
            }
        }
    }

    fun removeExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseSelectionRepository.removeExercise(exercise = exercise.mockExercise)
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
                    timeStart = _state.value.date.timeStart,
                    timeEnd = _state.value.date.timeStart
                )
                val trainId = async { trainUseCase.createTrain(train) }

                with(trainId.await()) {
                    val exercises = _state.value.exercises.map {
                        it.copy(trainId = this.toInt()).toEntity()
                    }

                    exerciseUseCase.insertExercises(exercises)
                }
            }
        }
    }

    fun updateDay(day: Date) {
        viewModelScope.executeUI {
            _state.emit(
                _state.value.copy(
                    date = _state.value.date.copy(
                        day = day
                    )
                )
            )
        }
    }

    fun updateTime(hour: Int, minute: Int, pickedTime: PickedTime) {
        when (pickedTime) {
            PickedTime.START -> {
                viewModelScope.executeUI {
                    _state.emit(
                        _state.value.copy(
                            date = _state.value.date.copy(
                                timeStart = _state.value.date.timeStart.newTime(hour, minute)
                            )
                        )
                    )
                }
            }
            PickedTime.END -> {
                viewModelScope.executeUI {
                    _state.emit(
                        _state.value.copy(
                            date = _state.value.date.copy(
                                timeEnd = _state.value.date.timeEnd.newTime(hour, minute)
                            )
                        )
                    )
                }
            }
        }
    }

    fun addExercises() {
        viewModelScope.launch {
            val exercises = _state.value.exercises.map { it.mockExercise }
            if (exercises.isNotEmpty()) {
                exerciseSelectionRepository.addPreSelectedExercises(exercises = exercises)
            }
        }
    }

    data class State(
        val client: Client = Client(),
        val exercises: List<Exercise> = emptyList(),
        val date: DateState)

    data class DateState(val day: Date, val timeStart: Date, val timeEnd: Date)
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