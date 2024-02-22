package fr.group5.magellangpt.presentation.main

import androidx.lifecycle.ViewModel
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.presentation.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.java.KoinJavaComponent.get

class MainViewModel(
    private val navigator : Navigator = get(Navigator::class.java)
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
        val messages = listOf(
            Message(
                content = "Welcome to Magellan GPT",
                sender = MessageSender.USER),
            Message(
                content = "I am Magellan, your personal guide",
                sender = MessageSender.AI)
        )

        _uiState.update { it.copy(messages = messages) }
    }


    private fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    private fun onLogout(){
        navigator.navigateTo(
            route = Navigator.Destination.Login,
            popupTo = Navigator.Destination.Main.route,
            inclusive = true
        )
    }
}