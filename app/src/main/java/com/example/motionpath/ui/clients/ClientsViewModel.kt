package com.example.motionpath.ui.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.motionpath.domain.usecase.client.ClientUseCase
import com.example.motionpath.model.domain.client_category.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(private val clientUseCase: ClientUseCase) : ViewModel() {

    private val _clients = MutableStateFlow(emptyList<ClientCategory>())
    val clients = _clients.asLiveData()

    init {
        loadClients()
    }

    private fun loadClients() {
        viewModelScope.launch {
            val clientCategories: ArrayList<ClientCategory> = ArrayList()
            clientUseCase.getClients()
                .collect { list ->
                    CategoryType.values().map { type ->
                        list.count { client -> client.categoryType == type }
                            .also { count -> clientCategories.add(ClientCategory(type, count)) }
                    }

                    _clients.emit(clientCategories)
                }
        }
    }
}

data class ClientCategory(val category: CategoryType, val count: Int)