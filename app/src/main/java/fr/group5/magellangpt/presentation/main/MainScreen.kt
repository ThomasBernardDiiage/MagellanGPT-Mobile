package fr.group5.magellangpt.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.presentation.components.main.IAMessage
import fr.group5.magellangpt.presentation.components.main.UserMessage
import fr.thomasbernard03.composents.TextField
import fr.thomasbernard03.composents.buttons.SquaredButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onEvent: (MainEvent) -> Unit
){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val options by remember { mutableStateOf(listOf("GPT 3.5", "GPT 4")) }
    var selectedOptionIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit){
        onEvent(MainEvent.OnAppearing)
    }

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

                    HorizontalDivider()

                    Text(
                        text = stringResource(id = R.string.conversations),
                        modifier = Modifier.padding(vertical = 16.dp))

                    HorizontalDivider()


                    Spacer(modifier = Modifier.weight(1f))


                    Row {
                        SquaredButton(
                            resource = R.drawable.logout,
                            onClick = { onEvent(MainEvent.OnLogout)}
                        )
                    }

                }

            }
        }
    ) {

        Column(
            modifier = Modifier
                .background(Color(0xFFF7F7F7))
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                SquaredButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = Color.Transparent,
                    resource = R.drawable.vectormenu,
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    })

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .align(Alignment.Center),
                ) {
                    options.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = selectedOptionIndex == index,
                            onClick = { selectedOptionIndex = index},
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)) {
                            Text(text = option)
                        }
                    }

                }

            }

            if (uiState.messages.isNotEmpty()){
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.messages){ message ->
                        when(message.sender){
                            MessageSender.USER ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 12.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    UserMessage(content = message.content)
                                }
                            MessageSender.AI ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 12.dp),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    IAMessage(content = message.content)
                                }
                        }
                    }
                }
            }
            else {
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

                SquaredButton(
                    resource = R.drawable.file_icon,
                    onClick = {

                    })
            }

        }
    }
}