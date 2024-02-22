package fr.group5.magellangpt.presentation.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import fr.group5.magellangpt.presentation.theme.Primary
import fr.group5.magellangpt.presentation.theme.Secondary

@Composable
fun Message(
    content : String,
    isUser : Boolean = true
) {
    MarkdownText(
        style = TextStyle(color = Color.White),
        modifier = Modifier
            .background(if (isUser) Primary else Secondary, shape = RoundedCornerShape(8.dp))
            .padding(12.dp) ,
        markdown = content)
}

@Composable
@Preview
fun Message() {
    Message("Hello World!", isUser = true)
}
