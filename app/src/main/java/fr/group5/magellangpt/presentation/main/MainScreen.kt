package fr.group5.magellangpt.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.thomasbernard03.composents.TextField
import fr.thomasbernard03.composents.buttons.SquaredButton
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    uiState: MainUiState,
    onEvent: (MainEvent) -> Unit
){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier.padding(vertical = 16.dp))

                    TextField(
                        text = "",
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = stringResource(id = R.string.search_conversation),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "search")
                        })

                    Text(
                        text = stringResource(id = R.string.my_rag),
                        modifier = Modifier.padding(vertical = 16.dp))

                    Divider()

                    Text(
                        text = stringResource(id = R.string.conversations),
                        modifier = Modifier.padding(vertical = 16.dp))

                    Divider()

                }

            }
        }) {
        Column(
            modifier = Modifier
                .background(Color(0xFFF7F7F7))
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                SquaredButton(
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = Color.Transparent,
                    resource = R.drawable.vectormenu,
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    })
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.file),
                    contentDescription = "file")

                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.empty_conversation_placeholder),
                    style = MaterialTheme.typography.titleSmall)
            }


            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    placeholder = stringResource(id = R.string.message),
                    modifier = Modifier.weight(1f),
                    text = uiState.query,
                    onTextChange = { onEvent(MainEvent.OnQueryChanged(it)) })

                SquaredButton(resource = R.drawable.share, onClick = {  })
            }

        }
    }
}