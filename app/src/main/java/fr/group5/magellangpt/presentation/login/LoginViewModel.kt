package fr.group5.magellangpt.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.helpers.NavigationHelper
import fr.group5.magellangpt.domain.usecases.LoginUseCase
import fr.group5.magellangpt.domain.usecases.UserConnectedUseCase
import fr.group5.magellangpt.presentation.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class LoginViewModel(
    private val navigationHelper : NavigationHelper = get(NavigationHelper::class.java),
    private val loginUseCase: LoginUseCase = get(LoginUseCase::class.java),
    private val userConnectedUseCase: UserConnectedUseCase = get(UserConnectedUseCase::class.java),
    private val errorHelper: ErrorHelper = get(ErrorHelper::class.java),
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.OnAppearing -> onAppearing()
            is LoginEvent.OnLogin -> onLogin(event.activity)
        }
    }

    private fun onAppearing(){
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            userConnectedUseCase { connected ->
                if (connected) {
                    navigationHelper.navigateTo(
                        route = NavigationHelper.Destination.Main,
                        popupTo = NavigationHelper.Destination.Login.route,
                        inclusive = true)
                }
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun onLogin(activity : MainActivity){
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            loginUseCase(
                activity = activity,
                onSuccess = {
                    navigationHelper.navigateTo(
                        route = NavigationHelper.Destination.Main,
                        popupTo = NavigationHelper.Destination.Login.route,
                        inclusive = true)

                    _uiState.update { it.copy(loading = false) }
                },
                onError = { error ->
                    Log.e("LoginViewModel", error)
                    viewModelScope.launch { errorHelper.onError(ErrorHelper.Error(error)) }
                    _uiState.update { it.copy(loading = false) }
                },
                onCancel = {
                    _uiState.update { it.copy(loading = false) }
                })
        }
    }
}