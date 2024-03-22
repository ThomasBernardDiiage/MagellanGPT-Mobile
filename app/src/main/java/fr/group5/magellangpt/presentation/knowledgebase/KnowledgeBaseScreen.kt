package fr.group5.magellangpt.presentation.knowledgebase

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.login.PrimaryButton
import fr.group5.magellangpt.presentation.main.MainEvent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KnowledgeBaseScreen(uiState : KnowledgeBaseUiState, onEvent : (KnowledgeBaseEvent) -> Unit){

    val fileResult = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        fileResult.value = it

        it?.let {
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FlowRow(modifier = Modifier.weight(1f)) {

        }

        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            PrimaryButton(
                text = stringResource(id = R.string.add_file),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                launcher.launch(arrayOf("application/pdf"))
            }
        }
    }
    

    
    
    
}


@Composable
@Preview
private fun KnowledgeBaseScreenPreview() {
    KnowledgeBaseScreen(uiState = KnowledgeBaseUiState(), onEvent = {})
}
