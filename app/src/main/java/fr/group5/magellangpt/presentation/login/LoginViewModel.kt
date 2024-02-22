package fr.group5.magellangpt.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.MediaPlayerHelper
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.usecases.AuthenticationUseCase
import fr.group5.magellangpt.presentation.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class LoginViewModel(
    private val navigator : Navigator = get(Navigator::class.java),
    private val authenticationUseCase: AuthenticationUseCase = get(AuthenticationUseCase::class.java),
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.OnLogin -> onLogin(event.activity)
        }
    }

    private fun onLogin(activity : MainActivity){
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            when(val result = authenticationUseCase.login(activity)){
                is Resource.Success -> {
                    navigator.navigateTo(
                        route = Navigator.Destination.Main,
                        popupTo = Navigator.Destination.Login.route,
                        inclusive = true)

                    _uiState.update { it.copy(loading = false) }
                }
                is Resource.Error -> {
                    Log.e("LoginViewModel", "Error: ${result.message}")
                    _uiState.update { it.copy(loading = false) }
                }
            }
        }
    }
}