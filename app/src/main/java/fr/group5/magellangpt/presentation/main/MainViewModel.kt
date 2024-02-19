package fr.group5.magellangpt.presentation.main

import androidx.lifecycle.ViewModel
import fr.group5.magellangpt.presentation.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


    fun onEvent(event : MainEvent){
        when(event){
            is MainEvent.OnQueryChanged -> onQueryChanged(event.query)
        }
    }

    private fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }
}