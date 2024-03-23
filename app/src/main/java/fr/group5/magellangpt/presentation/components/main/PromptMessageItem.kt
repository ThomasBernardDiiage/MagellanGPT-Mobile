package fr.group5.magellangpt.presentation.components.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import fr.group5.magellangpt.presentation.theme.Blue

@Composable
fun PromptMessageItem(
    modifier : Modifier = Modifier,
    content : String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomEnd = 8.dp,
            bottomStart = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Blue.copy(alpha = 0.2f)
        ),
    ) {
            MarkdownText(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                style = TextStyle(color = Color.White, textAlign = TextAlign.Center),
                markdown = content)
    }
}