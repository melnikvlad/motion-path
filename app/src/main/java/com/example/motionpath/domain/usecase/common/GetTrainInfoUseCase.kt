package com.example.motionpath.domain.usecase.common

import com.example.motionpath.domain.usecase.client.GetClientUseCase
import com.example.motionpath.domain.usecase.train.GetTrainExercisesUseCase
import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.ui.create_train.Client
import com.example.motionpath.ui.create_train.Mode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GetTrainInfoUseCase @Inject constructor(
    private val getTrainExercisesUseCase: GetTrainExercisesUseCase,
    private val clientUseCase: GetClientUseCase
) {

    suspend operator fun invoke(
        scope: CoroutineScope,
        mode: Mode,
        clientId: Int,
        trainId: Int
    ): Pair<Client, List<Exercise>> {

        val job = scope.async {
            val client = async {
                if (clientId != -1) {
                    clientUseCase(clientId)
                } else {
                    Client()
                }
            }

            val exercises = async {
                if (mode == Mode.EDIT) {
                    getTrainExercisesUseCase(trainId)
                } else {
                    emptyList()
                }
            }

            Pair(client.await() ?: Client(), exercises.await())
        }

        return job.await()
    }
}