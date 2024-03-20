package fr.group5.magellangpt.presentation.components.main

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.extensions.toPrettyHour
import fr.group5.magellangpt.presentation.theme.MagellanGPTTheme
import fr.group5.magellangpt.presentation.theme.Secondary
import fr.group5.magellangpt.presentation.theme.Typography
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AiMessageItem(
    content : String,
    date : Date,
    model : String
) {

    val clipboardManager: androidx.compose.ui.platform.ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    fun copyToClipboard() {
        clipboardManager.setText(AnnotatedString((content)))
        Toast
            .makeText(context, R.string.copied_into_clipboard, Toast.LENGTH_SHORT)
            .show()
    }

    fun copyCodeToClipboard() {
        val code = content.substringAfter("```").substringBefore("```")
        clipboardManager.setText(AnnotatedString((code)))
        Toast
            .makeText(context, R.string.code_copied_into_clipboard, Toast.LENGTH_SHORT)
            .show()
    }

    Card(
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomEnd = 8.dp,
            bottomStart = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Secondary
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    if (content.contains("```")){
                        copyCodeToClipboard()
                    }
                    else {
                        copyToClipboard()
                    }
                }
            )
    ) {
        Column(modifier = Modifier.padding(PaddingValues(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 6.dp))) {
            MarkdownText(
                modifier = Modifier.pointerInput(Unit) { },
                style = TextStyle(color = Color.White),
                markdown = content)

            Text(
                text = "${date.toPrettyHour()} - $model",
                color = Color.White,
                style = Typography.labelSmall)
        }
    }
}

@Composable
@Preview
private fun AiMessageItemPreview() {
    MagellanGPTTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AiMessageItem(content = "Hello World!", date = Date(), model = "GPT-3")
                AiMessageItem(content = "Hello World!",date = Date(), model = "GPT-3")
            }
        }
    }
}
