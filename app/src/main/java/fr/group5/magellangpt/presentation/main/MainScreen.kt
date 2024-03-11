package fr.group5.magellangpt.presentation.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.extensions.toDateLabel
import fr.group5.magellangpt.common.extensions.toPrettyDate
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.presentation.components.main.MainModalDrawerSheet
import fr.group5.magellangpt.presentation.components.main.Message
import fr.thomasbernard03.composents.TextField
import fr.thomasbernard03.composents.buttons.SquaredButton
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onEvent: (MainEvent) -> Unit
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val options by remember { mutableStateOf(listOf("GPT 3.5", "GPT 4")) }
    var selectedOptionIndex by remember { mutableStateOf(0) }

    LaunchedEffect(uiState.messages) {
        if (uiState.messages.isNotEmpty()) {
            lazyListState.scrollToItem(uiState.messages.size - 1)
        }
    }


    val fileResult = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        fileResult.value = it
    }

    LaunchedEffect(Unit){
        onEvent(MainEvent.OnAppearing)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MainModalDrawerSheet(
                firstname = uiState.firstname,
                lastname = uiState.lastname,
                email = uiState.email,
                query = uiState.conversationQuery,
                onLogout = { onEvent(MainEvent.OnLogout) },
                onClose = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .background(Color(0xFFF7F7F7))
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    options.forEachIndexed { index, option ->
                        SegmentedButton(
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primary,
                                activeContentColor = Color.White
                            ),
                            selected = selectedOptionIndex == index,
                            onClick = { selectedOptionIndex = index},
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        ) {
                            Text(text = option)
                        }
                    }

                }

                Box(modifier = Modifier.size(44.dp))

            }

            if (uiState.messages.isNotEmpty()){
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.messages.forEach { date, messages ->
                        stickyHeader {
                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(Color(0xFFF7F7F7))
                                    .fillMaxWidth(),
                                text = date.toDateLabel(),
                            )
                        }

                        items(messages){ message ->
                            when(message.sender){
                                MessageSender.USER ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 12.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Message(content = message.content, date = message.date, isUser = true)
                                    }
                                MessageSender.AI ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 12.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Message(content = message.content, date = message.date, isUser = false)
                                    }
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
                SquaredButton(
                    resource = R.drawable.file_icon,
                    onClick = { launcher.launch(arrayOf("application/pdf")) })

                TextField(
                    placeholder = stringResource(id = R.string.message),
                    modifier = Modifier.weight(1f),
                    text = uiState.message,
                    keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                    onTextChange = { onEvent(MainEvent.OnQueryChanged(it)) })


                SquaredButton(
                    resource = R.drawable.send,
                    onClick = {
                        onEvent(MainEvent.OnSendMessage(uiState.message))
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun mainScreenEmptyMessagePreview(){
    val uiState = MainUiState()
    MainScreen(uiState = uiState, onEvent = {})
}

@Composable
@Preview
private fun mainScreenMessagesPreview(){
    val messages = listOf(
        fr.group5.magellangpt.domain.models.Message(id = 1, content = "Hello there", sender = MessageSender.USER, date = Date()),
        fr.group5.magellangpt.domain.models.Message(id = 2, content = "Hello obi-wan", sender = MessageSender.AI, date = Date()),
    )
    val uiState = MainUiState(messages= messages.groupBy { it.date })
    MainScreen(uiState = uiState, onEvent = {})
}