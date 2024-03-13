package fr.group5.magellangpt.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.helpers.NavigationHelper
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.usecases.GetConversationUseCase
import fr.group5.magellangpt.domain.usecases.GetCurrentUserUseCase
import fr.group5.magellangpt.domain.usecases.LogoutUseCase
import fr.group5.magellangpt.domain.usecases.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class MainViewModel(
    private val logoutUseCase: LogoutUseCase = get(LogoutUseCase::class.java),
    private val navigationHelper : NavigationHelper = get(NavigationHelper::class.java),
    private val getConversationUseCase: GetConversationUseCase = get(GetConversationUseCase::class.java),
    private val getCurrentUserUseCase: GetCurrentUserUseCase = get(GetCurrentUserUseCase::class.java),
    private val sendMessageUseCase: SendMessageUseCase = get(SendMessageUseCase::class.java),
    private val errorHelper: ErrorHelper = get(ErrorHelper::class.java)
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onEvent(event : MainEvent){
        when(event){
            is MainEvent.OnAppearing -> onAppearing()
            is MainEvent.OnQueryChanged -> onQueryChanged(event.query)
            is MainEvent.OnLogout -> onLogout()
            is MainEvent.OnSendMessage -> onSendMessage(event.message)
        }
    }

    private fun onAppearing(){
        viewModelScope.launch {
            when(val result = getConversationUseCase()){
                is Resource.Success -> {
                    result.data.collectLatest { messages ->
                        _uiState.update { it.copy(messages = messages) }
                    }
                }
                is Resource.Error -> {
                    Log.e("MainViewModel", "An error occurred")
                }
            }
        }

        viewModelScope.launch {
            getCurrentUserUseCase { user ->
                _uiState.update { it.copy(firstname = user.firstname, lastname = user.lastname, email = user.email) }
            }
        }
    }


    private fun onSendMessage(message : String){
        viewModelScope.launch {
            if (message.isBlank()) return@launch

            _uiState.update { it.copy(message = "", typing = true) }

            when(val result = sendMessageUseCase(message)){
                is Resource.Success -> {
                    _uiState.update { it.copy(typing = false) }
                }
                is Resource.Error -> {
                    errorHelper.onError(ErrorHelper.Error(message = result.message))
                    _uiState.update { it.copy(typing = false) }

                }
            }
        }
    }


    private fun onQueryChanged(query: String) {
        _uiState.update { it.copy(message = query) }
    }

    private fun onLogout(){
        viewModelScope.launch {
            logoutUseCase()

            navigationHelper.navigateTo(
                route = NavigationHelper.Destination.Login,
                popupTo = NavigationHelper.Destination.Main.route,
                inclusive = true)
        }
    }
}