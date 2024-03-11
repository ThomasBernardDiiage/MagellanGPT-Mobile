package fr.group5.magellangpt.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.usecases.GetConversationUseCase
import fr.group5.magellangpt.domain.usecases.GetCurrentUserUseCase
import fr.group5.magellangpt.domain.usecases.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class MainViewModel(
    private val logoutUseCase: LogoutUseCase = get(LogoutUseCase::class.java),
    private val navigator : Navigator = get(Navigator::class.java),
    private val getConversationUseCase: GetConversationUseCase = get(GetConversationUseCase::class.java),
    private val getCurrentUserUseCase: GetCurrentUserUseCase = get(GetCurrentUserUseCase::class.java),
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onEvent(event : MainEvent){
        when(event){
            is MainEvent.OnAppearing -> onAppearing()
            is MainEvent.OnQueryChanged -> onQueryChanged(event.query)
            is MainEvent.OnLogout -> onLogout()
        }
    }

    private fun onAppearing(){
        viewModelScope.launch {
            when(val result = getConversationUseCase()){
                is Resource.Success -> {
                    _uiState.update { it.copy(messages = result.data) }
                }
                is Resource.Error -> {
                    Log.e("MainViewModel", "An error occurred")
                }
            }

            getCurrentUserUseCase { user ->
                _uiState.update { it.copy(firstname = user.firstname, lastname = user.lastname, email = user.email) }
            }
        }
    }


    private fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    private fun onLogout(){
        viewModelScope.launch {
            logoutUseCase()

            navigator.navigateTo(
                route = Navigator.Destination.Login,
                popupTo = Navigator.Destination.Main.route,
                inclusive = true)
        }
    }
}