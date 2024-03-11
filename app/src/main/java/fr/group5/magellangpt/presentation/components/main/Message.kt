package fr.group5.magellangpt.presentation.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import fr.group5.magellangpt.common.extensions.toPrettyDate
import fr.group5.magellangpt.presentation.theme.MagellanGPTTheme
import fr.group5.magellangpt.presentation.theme.Primary
import fr.group5.magellangpt.presentation.theme.Secondary
import java.util.Date
import fr.group5.magellangpt.presentation.theme.Typography

@Composable
fun Message(
    content : String,
    date : Date,
    isUser : Boolean = true
) {
    Column(
        modifier = Modifier
            .background(
                color = if (isUser) Primary else Secondary,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomEnd = if (isUser) 0.dp else 8.dp, bottomStart = if (isUser) 8.dp else 0.dp)
            )
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp, bottom = 6.dp)
    ) {
        MarkdownText(
            style = TextStyle(color = Color.White),
            markdown = content)

        Text(
            text = date.toPrettyDate(),
            color = Color.White,
            style = Typography.labelSmall
        )
    }

}

@Composable
@Preview
fun Message() {
    MagellanGPTTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Message(content = "Hello World!", date = Date(), isUser = true)
                Message(content = "Hello World!",date = Date(), isUser = false)
            }
        }
    }
}
