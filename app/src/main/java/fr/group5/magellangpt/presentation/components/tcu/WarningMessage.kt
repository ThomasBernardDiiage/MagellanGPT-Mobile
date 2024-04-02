package fr.group5.magellangpt.presentation.components.tcu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.PreviewComponents
import fr.group5.magellangpt.presentation.theme.Secondary

@Composable
fun WarningMessage(
    modifier : Modifier = Modifier,
    text : String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Secondary.copy(0.2f),
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = R.drawable.info),
                contentDescription = "Info",
                tint = Secondary)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                textAlign = TextAlign.Center,
                text = text,
                color = Secondary,
                style = MaterialTheme.typography.bodyMedium)
        }

    }
}

@Composable
@Preview
private fun WarningMessagePreview() = PreviewComponents {
    WarningMessage(text = "This is a warning message.")
}