package fr.group5.magellangpt.presentation.knowledgebase

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.knowledgebase.DocumentPreview
import fr.group5.magellangpt.presentation.components.knowledgebase.KnowledgeBaseDescription
import fr.group5.magellangpt.presentation.components.login.PrimaryButton
import fr.group5.magellangpt.presentation.main.MainEvent

@Composable
fun KnowledgeBaseScreen(uiState : KnowledgeBaseUiState, onEvent : (KnowledgeBaseEvent) -> Unit){

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        it?.let { onEvent(KnowledgeBaseEvent.OnDocumentLoaded(it)) }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        KnowledgeBaseDescription(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp)
        )
        
        if (uiState.document != null){
            DocumentPreview(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                uri = uiState.document.first,
                document = uiState.document.second
            ) {
                onEvent(KnowledgeBaseEvent.OnDocumentDeleted)
            }
        }
        else {
            Spacer(modifier = Modifier.weight(1f))
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                loading = uiState.loading,
                text = if (uiState.document == null) stringResource(id = R.string.add_file) else stringResource(id = R.string.send_file),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (uiState.document == null){
                    launcher.launch(arrayOf("application/pdf"))
                }
                else {
                    onEvent(KnowledgeBaseEvent.OnUploadDocument(uiState.document.first))
                }
            }
        }
    }
}


@Composable
@Preview
private fun KnowledgeBaseScreenPreview() {
    KnowledgeBaseScreen(uiState = KnowledgeBaseUiState(), onEvent = {})
}
