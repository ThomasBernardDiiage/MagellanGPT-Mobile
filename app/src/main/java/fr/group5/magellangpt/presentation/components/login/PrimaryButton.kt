package fr.group5.magellangpt.presentation.components.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.presentation.theme.MagellanGPTTheme


/**
 * Primary button
 * @param modifier Modifier -> Modifier of the button
 * @param text String -> Text of the button display if not loading
 * @param loading Boolean -> If the button is loading show a circular progress indicator
 * @param onClick () -> Unit -> Click callback
 */
@Composable
fun PrimaryButton(
    modifier : Modifier = Modifier,
    text: String,
    loading : Boolean = false,
    onClick: () -> Unit
) {
    val height = 60.dp

    Button(
        onClick = {
            if (!loading)
                onClick()
        },
        modifier = Modifier
            .height(height)
            .then(
                if (loading)
                    Modifier.width(height)
                else
                    Modifier
            )
            .then(modifier),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(if (loading) height/2 else 40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .height(25.dp)
                    .width(25.dp),
                strokeWidth = 3.dp)
        }
        else {
            Text(
                text,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {

    MagellanGPTTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Sign in with Microsoft",
                    onClick = {})

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Sign in with Microsoft",
                    loading = true,
                    onClick = {})
            }
        }
    }
}
