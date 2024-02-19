package fr.group5.magellangpt.presentation.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.login.PrimaryButton
import fr.group5.magellangpt.ui.theme.Blue
import fr.group5.magellangpt.ui.theme.MagellanGPTTheme

@Composable
fun LoginScreen() {
    Scaffold {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Blue,
                                Color.White)
                        )
                    )
            ) {


                Image(
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).zIndex(2f),
                    painter = painterResource(id = R.drawable.login_icon),
                    contentDescription = "")

                Column(
                    modifier = Modifier
                        .padding(top = 360.dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(topStart = 40.dp))
                        .padding(36.dp)
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_to),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge)

                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(id = R.string.login_subtitle),
                        style = MaterialTheme.typography.titleSmall)

                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.login_with_microsoft),
                        loading = false,
                        onClick = {})
                }
            }
        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview() {
    MagellanGPTTheme {
        Surface {
            LoginScreen()
        }
    }
}