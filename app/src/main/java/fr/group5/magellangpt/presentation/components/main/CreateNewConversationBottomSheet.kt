package fr.group5.magellangpt.presentation.components.main

import android.view.Gravity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.TextField
import fr.thomasbernard03.composents.buttons.PrimaryButton


@Composable
fun CreateNewConversationBottomSheet(
    conversationName: String,
    conversationPrePrompt: String,
    createConversationLoading : Boolean,
    onClose : () -> Unit,
    onConversationNameChanged : (String) -> Unit = {},
    onPrePromptChanged : (String) -> Unit = {},
    onValidate : (conversationName : String, prePrompt : String) -> Unit = { _, _ -> }
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(
        onDismissRequest = {
            if (!createConversationLoading)
                onClose()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        // Gravity bottom

        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)


        Surface(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 24.sp,
                    text = stringResource(id = R.string.create_new_conversation),
                    style = MaterialTheme.typography.titleMedium)

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    text = conversationName,
                    placeholder = stringResource(id = R.string.conversation_name),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, capitalization = KeyboardCapitalization.Sentences),
                    maxLength = 50,
                    onTextChange = {
                        onConversationNameChanged(it)
                    }
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    text = conversationPrePrompt,
                    placeholder = stringResource(id = R.string.pre_prompt),
                    maxLength = 200,
                    keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                    singleLine = false,
                    onTextChange = {
                        onPrePromptChanged(it)
                    },
                    maxLines = 10
                )

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.create),
                    loading = createConversationLoading,
                    enabled = conversationName.isNotEmpty()
                ) {
                    onValidate(conversationName, conversationPrePrompt)
                }
            }
        }
    }
}



@Preview
@Composable
private fun CreateNewConversationBottomSheetPreview() {
    CreateNewConversationBottomSheet(
        conversationName = "",
        conversationPrePrompt = "",
        createConversationLoading = true,
        onClose = {}
    )
}