package fr.group5.magellangpt.presentation.knowledgebase

import android.net.Uri
import com.pspdfkit.document.PdfDocument

data class KnowledgeBaseUiState(
    val loading : Boolean = false,
    val document : Pair<Uri, PdfDocument>? = null,
)