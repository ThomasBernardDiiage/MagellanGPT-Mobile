package fr.group5.magellangpt.presentation.main

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pspdfkit.document.PdfDocument
import com.pspdfkit.document.PdfDocumentLoader
import fr.group5.magellangpt.common.extensions.to6AM
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
import fr.group5.magellangpt.domain.usecases.GetMessagesUseCase
import fr.group5.magellangpt.domain.usecases.LogoutUseCase
import fr.group5.magellangpt.domain.usecases.PostMessageInConversationUseCase
import fr.group5.magellangpt.domain.usecases.PostMessageInNewConversationUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainViewModel(
    private val logoutUseCase: LogoutUseCase = get(LogoutUseCase::class.java),
    private val navigationHelper : NavigationHelper = get(NavigationHelper::class.java),
    private val getConversationUseCase: GetConversationUseCase = get(GetConversationUseCase::class.java),
    private val getCurrentUserUseCase: GetCurrentUserUseCase = get(GetCurrentUserUseCase::class.java),
    private val postMessageInConversationUseCase: PostMessageInConversationUseCase = get(PostMessageInConversationUseCase::class.java),
    private val errorHelper: ErrorHelper = get(ErrorHelper::class.java),
    private val getAvailableModelsUseCase: GetAvailableModelsUseCase = get(GetAvailableModelsUseCase::class.java),
    private val preferencesHelper: PreferencesHelper = get(PreferencesHelper::class.java),
    private val getConversationsUseCase: GetConversationsUseCase = get(GetConversationsUseCase::class.java),
    private val postMessageInNewConversationUseCase: PostMessageInNewConversationUseCase = get(PostMessageInNewConversationUseCase::class.java),
    private val getMessagesUseCase: GetMessagesUseCase = get(GetMessagesUseCase::class.java),
    private val context : Context = get(Context::class.java),
    private val ioDispatcher : CoroutineDispatcher = get(CoroutineDispatcher::class.java)
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getMessagesUseCase()
                .collectLatest { messages ->
                    _uiState.update { it.copy(messages = messages.filter { it.content.isNotBlank() }.groupBy { it.date.to6AM() }) }
                }
        }
    }

    fun onEvent(event : MainEvent){
        when(event){
            is MainEvent.OnAppearing -> onAppearing()
            is MainEvent.OnMessageChanged -> onQueryChanged(event.message)
            is MainEvent.OnLogout -> onLogout()
            is MainEvent.OnSendMessage -> onSendMessage(event.message)
            is MainEvent.OnModelSelected -> onModelSelected(event.model)
            is MainEvent.OnConversationQueryChanged -> onConversationQueryChanged(event.query)
            is MainEvent.OnConversationSelected -> onConversationSelected(event.conversation)
            is MainEvent.OnConversationsRefreshed -> refreshConversations()
            is MainEvent.OnDocumentLoaded -> onDocumentLoaded(event.uri)
            is MainEvent.OnDocumentDeleted -> onDocumentDeleted(event.uri)
        }
    }

    private fun onAppearing(){
        getUserInformation()
        getModels()
        getConversations()
    }


    private fun onSendMessage(message : String){
        viewModelScope.launch {
            if (message.isBlank()) return@launch

            _uiState.update { it.copy(message = "", typing = true) }

            if (_uiState.value.selectedConversation == null){
                when(val result = postMessageInNewConversationUseCase(message)){
                    is Resource.Success -> {
                        _uiState.update { it.copy(typing = false) }
                    }
                    is Resource.Error -> {
                        errorHelper.onError(ErrorHelper.Error(message = result.message))
                        _uiState.update { it.copy(typing = false) }
                    }
                }
            }
            else {
                when(val result = postMessageInConversationUseCase(_uiState.value.selectedConversation!!.id, message)){
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
        viewModelScope.launch {
            _uiState.update { it.copy(selectedConversation = conversation, messagesLoading = true) }
            when(val result = getConversationUseCase(conversation.id)){
                is Resource.Success -> {
                    _uiState.update { it.copy(messagesLoading = false) }
                }
                is Resource.Error -> {
                    errorHelper.onError(ErrorHelper.Error(message = result.message))
                    _uiState.update { it.copy(messagesLoading = false) }

                }
            }
        }
    }

    private fun getUserInformation(){
        viewModelScope.launch {
            getCurrentUserUseCase { user ->
                _uiState.update { it.copy(firstname = user.firstname, lastname = user.lastname, email = user.email) }
            }
        }
    }

    private fun getModels(){
        viewModelScope.launch {
            _uiState.update { it.copy(messagesLoading = true) }
            when(val result = getAvailableModelsUseCase()){
                is Resource.Success -> {
                    val selectedModel = result.data.firstOrNull { it.id == preferencesHelper.selectedModelId } ?: result.data.first()
                    preferencesHelper.selectedModelId = selectedModel.id
                    _uiState.update { it.copy(availableModel = result.data, selectedModel = selectedModel, messagesLoading = false) }
                }
                is Resource.Error -> {
                    errorHelper.onError(ErrorHelper.Error(message = result.message))
                    _uiState.update { it.copy(messagesLoading = false) }
                }
            }
        }
    }

    private fun refreshConversations(){
        viewModelScope.launch {
            _uiState.update { it.copy(conversationsRefreshing = true) }
            when(val result = getConversationsUseCase()){
                is Resource.Success -> {
                    _uiState.update { it.copy(conversations = result.data, conversationsRefreshing = false) }
                }
                is Resource.Error -> {
                    errorHelper.onError(ErrorHelper.Error(message = result.message))
                    _uiState.update { it.copy(conversationsRefreshing = false)}
                }
            }
        }
    }

    private fun getConversations(){
        viewModelScope.launch {
            _uiState.update { it.copy(conversationsLoading = true) }
            when(val result = getConversationsUseCase()){
                is Resource.Success -> {
                    _uiState.update { it.copy(conversations = result.data, conversationsLoading = false) }
                }
                is Resource.Error -> {
                    errorHelper.onError(ErrorHelper.Error(message = result.message))
                    _uiState.update { it.copy(conversationsLoading = false)}
                }
            }
        }
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

    // https://pspdfkit.com/blog/2021/open-pdf-in-jetpack-compose-app/
    private fun onDocumentLoaded(uri : Uri){
        viewModelScope.launch(ioDispatcher) {
            val pdfDocuments = loadPdf(context, uri)

            val documents = _uiState.value.documents.toMutableMap()
            documents[uri] = pdfDocuments

            _uiState.update { it.copy(documents = documents) }
        }
    }

    private fun onDocumentDeleted(uri : Uri){
        _uiState.update { it.copy(documents = _uiState.value.documents - uri) }
    }

    private suspend fun loadPdf(context: Context, uri: Uri) = suspendCoroutine<PdfDocument> { continuation ->
        PdfDocumentLoader
            .openDocumentAsync(context, uri)
            .subscribe(continuation::resume, continuation::resumeWithException)
    }
}