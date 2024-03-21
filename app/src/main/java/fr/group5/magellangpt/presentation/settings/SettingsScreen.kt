package fr.group5.magellangpt.presentation.settings

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.settings.SettingItem
import fr.thomasbernard03.composents.navigationbars.NavigationBar
import fr.group5.magellangpt.BuildConfig
import fr.group5.magellangpt.presentation.theme.Secondary

@Composable
fun SettingsScreen(
    uiState : SettingsUiState,
    onEvent : (SettingsEvent) -> Unit
) {

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        onEvent(SettingsEvent.OnGetUserInformation)
    }


    Scaffold(
        containerColor = Color.White,
        topBar = {
            NavigationBar(
                title = stringResource(id = R.string.settings),
                showBackButton = true,
                onBack = { onEvent(SettingsEvent.OnGoBack) },
                actions = { Box(modifier = Modifier.size(44.dp))}
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    style = MaterialTheme.typography.titleLarge,
                    text = "${stringResource(id = R.string.hello)} \uD83D\uDC4B")
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

            item {
                SettingItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = R.drawable.android,
                    title = R.string.app_name_android,
                    subtitle = "${stringResource(id = R.string.version)} ${BuildConfig.VERSION_NAME}"
                ) {

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