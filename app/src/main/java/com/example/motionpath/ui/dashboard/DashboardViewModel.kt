package com.example.motionpath.ui.dashboard

import androidx.lifecycle.*
import com.example.motionpath.data.db.SessionDao

class DashboardViewModel(private val sessionDao: SessionDao) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

}