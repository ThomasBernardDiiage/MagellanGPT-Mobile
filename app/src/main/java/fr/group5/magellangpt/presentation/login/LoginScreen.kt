package fr.group5.magellangpt.presentation.login

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.MainActivity
import fr.group5.magellangpt.presentation.components.login.PrimaryButton
import fr.group5.magellangpt.presentation.theme.Blue
import fr.group5.magellangpt.presentation.theme.MagellanGPTTheme

@Composable
fun LoginScreen(
    uiState : LoginUiState,
    onEvent : (LoginEvent) -> Unit
) {
    val activity = LocalContext.current as MainActivity

    val infiniteTransition = rememberInfiniteTransition(label = "Animation")
    val translateY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Animation"
    )

    LaunchedEffect(Unit){
        onEvent(LoginEvent.OnAppearing)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Blue, Color.White)))
    ) {
        Image(
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .graphicsLayer { translationY = translateY }
                .zIndex(1f),
            painter = painterResource(id = R.drawable.login_icon),
            contentDescription = "")

        Column(
            modifier = Modifier
                .padding(top = 360.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 40.dp)
                )
                .padding(36.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.welcome_to),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge)

            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.login_subtitle),
                style = MaterialTheme.typography.titleSmall)

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.login_with_microsoft),
                loading = uiState.loading,
                onClick = {
                    onEvent(LoginEvent.OnLogin(activity))
                })
        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview() {
    MagellanGPTTheme {
        Surface {
            val uiState = LoginUiState()
            LoginScreen(
                uiState = uiState,
                onEvent = {})
        }
    }
}