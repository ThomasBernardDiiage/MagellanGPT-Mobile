package fr.group5.magellangpt.presentation.knowledgebase

import androidx.lifecycle.ViewModel
import fr.group5.magellangpt.presentation.settings.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class KnowledgeBaseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(KnowledgeBaseUiState())
    val uiState: StateFlow<KnowledgeBaseUiState> = _uiState.asStateFlow()

    fun onEvent(event : KnowledgeBaseEvent){
        when(event){
            else -> {}
        }
    }
}