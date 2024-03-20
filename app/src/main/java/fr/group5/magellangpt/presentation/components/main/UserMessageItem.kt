package fr.group5.magellangpt.presentation.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import fr.group5.magellangpt.common.extensions.toPrettyHour
import fr.group5.magellangpt.presentation.theme.MagellanGPTTheme
import fr.group5.magellangpt.presentation.theme.Primary
import fr.group5.magellangpt.presentation.theme.Typography
import java.util.Date

@Composable
fun UserMessageItem(
    content : String,
    date : Date,
    filesNames : List<String>,
) {
    Card(
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomEnd = 0.dp,
            bottomStart = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Primary
        ),
    ) {
        Column(modifier = Modifier.padding(PaddingValues(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 6.dp))) {
            MarkdownText(
                modifier = Modifier.pointerInput(Unit) { },
                style = TextStyle(color = Color.White),
                markdown = content)

            filesNames.forEach { fileName ->
                Text(
                    text = "📎 $fileName",
                    color = Color.White,
                    style = Typography.labelSmall)
            }


            Text(
                text = date.toPrettyHour(),
                color = Color.White,
                style = Typography.labelSmall)
        }
    }
}

@Composable
@Preview
fun MessageItemPreview() {
    MagellanGPTTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserMessageItem(content = "Hello World!", date = Date(), emptyList())
                UserMessageItem(content = "Hello World!",date = Date(), emptyList())
            }
        }
    }
}
