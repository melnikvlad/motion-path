package com.example.motionpath.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.motionpath.data.db.ClientDao

class MainViewModelFactory(private val clientDao: ClientDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(clientDao) as T
    }
}