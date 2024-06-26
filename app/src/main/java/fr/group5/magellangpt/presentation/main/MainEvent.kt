package fr.group5.magellangpt.presentation.main

import android.net.Uri
import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.domain.models.Model

sealed class MainEvent {
    data object OnAppearing : MainEvent()
    data class OnMessageChanged(val message: String) : MainEvent()

    data class OnConversationQueryChanged(val query: String) : MainEvent()


    data class OnDocumentLoaded(val uri : Uri) : MainEvent()
    data class OnDocumentDeleted(val uri : Uri) : MainEvent()


    data class OnSendMessage(val message: String) : MainEvent()

    data object OnGoToSettings : MainEvent()

    data class OnModelSelected(val model: Model) : MainEvent()

    data class OnConversationSelected(val conversation: Conversation) : MainEvent()
    data object OnConversationsRefreshed : MainEvent()


    data class OnCreateConversationDialogVisibilityChanged(val show : Boolean) : MainEvent()
    data class OnCreateConversation(val conversationName : String, val prePrompt : String) : MainEvent()
    data class OnConversationNameChanged(val conversationName : String) : MainEvent()
    data class OnConversationPrePromptChanged(val prePrompt : String) : MainEvent()
}