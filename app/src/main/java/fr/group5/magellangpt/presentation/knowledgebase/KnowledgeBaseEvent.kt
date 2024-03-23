package fr.group5.magellangpt.presentation.knowledgebase

import android.net.Uri

sealed class KnowledgeBaseEvent {
    data class OnDocumentLoaded(val uri : Uri) : KnowledgeBaseEvent()
    data object OnDocumentDeleted : KnowledgeBaseEvent()

    data class OnUploadDocument(val uri : Uri) : KnowledgeBaseEvent()
}