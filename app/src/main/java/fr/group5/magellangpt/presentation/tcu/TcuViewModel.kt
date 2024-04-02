package fr.group5.magellangpt.presentation.tcu

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.usecases.AnalyseTcuUseCase
import fr.group5.magellangpt.domain.usecases.GetTcuQuestionsUseCase
import fr.group5.magellangpt.presentation.settings.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class TcuViewModel(
    private val getTcuQuestionsUseCase: GetTcuQuestionsUseCase = get(GetTcuQuestionsUseCase::class.java),
    private val errorHelper: ErrorHelper = get(ErrorHelper::class.java),
    private val analyseTcuUseCase: AnalyseTcuUseCase = get(AnalyseTcuUseCase::class.java)
) : ViewModel() {

    private val _uiState = MutableStateFlow(TcuUiState())
    val uiState: StateFlow<TcuUiState> = _uiState.asStateFlow()

    fun onEvent(event : TcuEvent){
        when(event){
            is TcuEvent.OnDocumentLoaded -> onDocumentLoaded(event.uri)
            is TcuEvent.OnLoadQuestions -> onLoadQuestions()
        }
    }

    private fun onDocumentLoaded(uri : Uri){
        viewModelScope.launch {
            val updatedQuestions = uiState.value.questions.map { it.copy(answer = "") }
            _uiState.update { it.copy(sendingDocument = true, questions = updatedQuestions ) }
            when(val result = analyseTcuUseCase(uri)){
                is Resource.Success -> {
                    _uiState.update { it.copy(sendingDocument = false, questions = result.data) }
                }
                is Resource.Error -> {
                    errorHelper.onError(ErrorHelper.Error(result.message))
                    _uiState.update { it.copy(sendingDocument = false) }
                }
            }
        }

    }

    private fun onLoadQuestions(){
        viewModelScope.launch {
            _uiState.update { it.copy(loadingQuestions = true) }
            when(val result = getTcuQuestionsUseCase()){
                is Resource.Success -> {
                    _uiState.update { it.copy(loadingQuestions = false, questions = result.data) }
                }
                is Resource.Error -> {
                    errorHelper.onError(ErrorHelper.Error(result.message))
                    _uiState.update { it.copy(loadingQuestions = false) }
                }
            }
        }
    }
}
