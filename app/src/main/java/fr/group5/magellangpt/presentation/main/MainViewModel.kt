package fr.group5.magellangpt.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.helpers.NavigationHelper
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.domain.models.Model
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.usecases.GetAvailableModelsUseCase
import fr.group5.magellangpt.domain.usecases.GetConversationUseCase
import fr.group5.magellangpt.domain.usecases.GetConversationsUseCase
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
    private val errorHelper: ErrorHelper = get(ErrorHelper::class.java),
    private val getAvailableModelsUseCase: GetAvailableModelsUseCase = get(GetAvailableModelsUseCase::class.java),
    private val preferencesHelper: PreferencesHelper = get(PreferencesHelper::class.java),
    private val getConversationsUseCase: GetConversationsUseCase = get(GetConversationsUseCase::class.java)

) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onEvent(event : MainEvent){
        when(event){
            is MainEvent.OnAppearing -> onAppearing()
            is MainEvent.OnMessageChanged -> onQueryChanged(event.message)
            is MainEvent.OnLogout -> onLogout()
            is MainEvent.OnSendMessage -> onSendMessage(event.message)
            is MainEvent.OnModelSelected -> onModelSelected(event.model)
            is MainEvent.OnConversationQueryChanged -> onConversationQueryChanged(event.query)
            is MainEvent.OnConversationSelected -> onConversationSelected(event.conversation)
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

        viewModelScope.launch {
            when(val result = getAvailableModelsUseCase()){
                is Resource.Success -> {
                    val selectedModel = result.data.firstOrNull { it.id == preferencesHelper.selectedModelId } ?: result.data.first()
                    _uiState.update { it.copy(availableModel = result.data, selectedModel = selectedModel) }
                }
                is Resource.Error -> errorHelper.onError(ErrorHelper.Error(message = result.message))
            }
        }

        viewModelScope.launch {
            when(val result = getConversationsUseCase()){
                is Resource.Success -> {
                    _uiState.update { it.copy(conversations = result.data) }
                }
                is Resource.Error -> errorHelper.onError(ErrorHelper.Error(message = result.message))
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

    private fun onModelSelected(model : Model){
        preferencesHelper.selectedModelId = model.id
        _uiState.update { it.copy(selectedModel = model) }
    }

    private fun onConversationQueryChanged(query : String){
        _uiState.update { it.copy(conversationQuery = query)}
    }

    private fun onConversationSelected(conversation : Conversation){
        _uiState.update { it.copy(selectedConversation = conversation) }
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