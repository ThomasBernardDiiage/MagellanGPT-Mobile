package fr.group5.magellangpt.presentation.tcu

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.Loader
import fr.group5.magellangpt.presentation.components.TextField
import fr.group5.magellangpt.presentation.components.login.PrimaryButton

@Composable
fun TcuScreen(uiState: TcuUiState, onEvent : (TcuEvent) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        it?.let { onEvent(TcuEvent.OnDocumentLoaded(it)) }
    }

    LaunchedEffect(Unit) {
        onEvent(TcuEvent.OnLoadQuestions)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (uiState.loadingQuestions){
            Loader(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                message = stringResource(id = R.string.loading_questions)
            )
        }
        else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(uiState.questions){ question ->
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = question.answer,
                        placeholder = question.question,
                        readOnly = true,
                        maxLines = 8,
                        singleLine = false
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                loading = uiState.sendingDocument,
                text = stringResource(id = R.string.add_file),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loadingQuestions
            ) {
                launcher.launch(arrayOf("application/pdf"))
            }
        }
    }
}