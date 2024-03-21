package fr.group5.magellangpt.presentation.knowledgebase

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun KnowledgeBaseScreen(uiState : KnowledgeBaseUiState, onEvent : (KnowledgeBaseEvent) -> Unit){
}


@Composable
@Preview
private fun KnowledgeBaseScreenPreview() {
    KnowledgeBaseScreen(uiState = KnowledgeBaseUiState(), onEvent = {})
}
