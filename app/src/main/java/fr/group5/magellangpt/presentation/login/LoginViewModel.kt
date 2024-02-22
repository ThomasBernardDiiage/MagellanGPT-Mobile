package fr.group5.magellangpt.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.MediaPlayerHelper
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class LoginViewModel(
    private val navigator : Navigator = get(Navigator::class.java),
) : ViewModel() {

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

            navigator.navigateTo(
                route = Navigator.Destination.Main,
                popupTo = Navigator.Destination.Login.route,
                inclusive = true)
        }
    }
}