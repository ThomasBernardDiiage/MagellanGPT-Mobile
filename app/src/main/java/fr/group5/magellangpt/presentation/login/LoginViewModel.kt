package fr.group5.magellangpt.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.OnLogin -> onLogin()
        }
    }


    private fun onLogin(){
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            delay(2000)
            _uiState.update { it.copy(loading = false) }
        }
    }
}