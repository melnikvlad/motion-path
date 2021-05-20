package com.example.motionpath.ui.dashboard

import androidx.lifecycle.*
import com.example.motionpath.data.db.ClientDao
import com.example.motionpath.model.entity.ClientEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DashboardViewModel(private val clientDao: ClientDao) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

}