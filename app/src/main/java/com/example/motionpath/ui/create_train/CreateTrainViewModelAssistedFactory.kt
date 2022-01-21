package com.example.motionpath.ui.create_train

import android.os.Bundle
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AssistedFactory
interface CreateTrainViewModelAssistedFactory {
    fun create(args: Bundle): CreateTrainViewModel
}