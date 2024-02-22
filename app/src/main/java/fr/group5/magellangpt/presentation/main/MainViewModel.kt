package fr.group5.magellangpt.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.usecases.AuthenticationUseCase
import fr.group5.magellangpt.domain.usecases.ConversationUseCase
import fr.group5.magellangpt.presentation.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class MainViewModel(
    private val authenticationUseCase: AuthenticationUseCase = get(AuthenticationUseCase::class.java),
    private val navigator : Navigator = get(Navigator::class.java),
    private val conversationUseCase: ConversationUseCase = get(ConversationUseCase::class.java),
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


    fun onEvent(event : MainEvent){
        when(event){
            is MainEvent.OnQueryChanged -> onQueryChanged(event.query)
            is MainEvent.OnAppearing -> onAppearing()
            is MainEvent.OnLogout -> onLogout()
        }
    }

    private fun onAppearing(){
        viewModelScope.launch {
            when(val result = conversationUseCase.getConversationMessages()){
                is Resource.Success -> {
                    _uiState.update { it.copy(messages = result.data) }
                }
                is Resource.Error -> {
                    Log.e("MainViewModel", "An error occurred")
                }
            }
        }


    }


    private fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    private fun onLogout(){
        viewModelScope.launch {
            when(val result = authenticationUseCase.logout()){
                is Resource.Success -> {
                    navigator.navigateTo(
                        route = Navigator.Destination.Login,
                        popupTo = Navigator.Destination.Main.route,
                        inclusive = true)
                }
                is Resource.Error -> {
                    Log.e("MainViewModel", result.message)
                }
            }
        }
    }
}