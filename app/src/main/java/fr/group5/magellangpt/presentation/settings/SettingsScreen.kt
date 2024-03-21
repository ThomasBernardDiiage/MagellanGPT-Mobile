package fr.group5.magellangpt.presentation.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.animation.content.Content
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.settings.SettingItem
import fr.thomasbernard03.composents.navigationbars.NavigationBar
import fr.group5.magellangpt.BuildConfig
import fr.group5.magellangpt.presentation.theme.Secondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    uiState : SettingsUiState,
    onEvent : (SettingsEvent) -> Unit
) {

    val uriHandler = LocalUriHandler.current
    var showConfetti by remember { mutableStateOf(false) }

    val preloaderLottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = 1,
        isPlaying = showConfetti
    )

    if (preloaderProgress == 1f){
        showConfetti = false
    }

    LaunchedEffect(Unit) {
        onEvent(SettingsEvent.OnGetUserInformation)
    }

    Box {
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    style = MaterialTheme.typography.titleLarge,
                    text = "${stringResource(id = R.string.hello)} \uD83D\uDC4B")
            }

            stickyHeader {
                Text(text = stringResource(id = R.string.about_me))
            }

            item {
                SettingItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = R.drawable.mail,
                    title = R.string.mail,
                    subtitle = uiState.email
                ) {

                }
            }

            item {
                SettingItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = R.drawable.user,
                    title = R.string.user,
                    subtitle = "${uiState.firstname} ${uiState.lastname.uppercase()}"
                ) {

                }
            }

            stickyHeader {
                Text(text = stringResource(id = R.string.settings))
            }

            item {
                SettingItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = R.drawable.brain,
                    title = R.string.knowledge_base,
                ) {
                    onEvent(SettingsEvent.OnGoToKnowledgeBase)
                }
            }


            item {
                SettingItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = R.drawable.info,
                    title = R.string.more_informations,
                ) {
                    val uri = "https://mango-wave-081aff710.5.azurestaticapps.net/main.html"
                    uriHandler.openUri(uri)
                }
            }


            stickyHeader {
                Text(text = stringResource(id = R.string.others))
            }

            item {
                SettingItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = R.drawable.android,
                    title = R.string.app_name_android,
                    subtitle = "${stringResource(id = R.string.version)} ${BuildConfig.VERSION_NAME}"
                ) {
                    showConfetti = true
                }
            }



            item {
                SettingItem(
                    color = Secondary,
                    modifier = Modifier.fillMaxWidth(),
                    icon = R.drawable.logout,
                    title = R.string.logout
                ) {
                    onEvent(SettingsEvent.OnLogout)
                }
            }
        }

        if (showConfetti){
            LottieAnimation(
                composition = preloaderLottieComposition,
                progress = preloaderProgress,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
@Preview
private fun SettingsScreenPreview(){
    SettingsScreen(
        uiState = SettingsUiState(),
        onEvent = {}
    )
}