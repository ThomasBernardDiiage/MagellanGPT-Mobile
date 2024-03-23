package fr.group5.magellangpt.presentation.knowledgebase

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pspdfkit.document.PdfDocument
import com.pspdfkit.document.PdfDocumentLoader
import fr.group5.magellangpt.presentation.settings.SettingsUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KnowledgeBaseViewModel(
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java),
    private val context: Context = get(Context::class.java),
) : ViewModel() {

    private val _uiState = MutableStateFlow(KnowledgeBaseUiState())
    val uiState: StateFlow<KnowledgeBaseUiState> = _uiState.asStateFlow()

    fun onEvent(event : KnowledgeBaseEvent){
        when(event){
            is KnowledgeBaseEvent.OnDocumentLoaded -> {
                viewModelScope.launch(ioDispatcher) {
                    val pdfDocuments = loadPdf(context, event.uri)

                    _uiState.update { it.copy(document = Pair(event.uri, pdfDocuments)) }
                }
            }
            is KnowledgeBaseEvent.OnDocumentDeleted -> {
                _uiState.update { it.copy(document = null) }
            }
        }
    }


    private suspend fun loadPdf(context: Context, uri: Uri) = suspendCoroutine<PdfDocument> { continuation ->
        PdfDocumentLoader
            .openDocumentAsync(context, uri)
            .subscribe(continuation::resume, continuation::resumeWithException)
    }
}