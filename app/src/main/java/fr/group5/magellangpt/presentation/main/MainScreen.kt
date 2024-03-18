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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.extensions.toDateLabel
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.presentation.components.Loader
import fr.group5.magellangpt.presentation.components.TextField
import fr.group5.magellangpt.presentation.components.main.EmptyList
import fr.group5.magellangpt.presentation.components.main.MainModalDrawerSheet
import fr.group5.magellangpt.presentation.components.main.MessageItem
import fr.group5.magellangpt.presentation.components.main.PdfThumbnail
import fr.group5.magellangpt.presentation.components.main.TypingMessage
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
    val context = LocalContext.current

    LaunchedEffect(uiState.messages) {
        if (uiState.messages.isNotEmpty()) {
            // Count all messages in the map
            lazyListState.scrollToItem(uiState.messages.flatMap { it.value }.size + uiState.messages.size - 1)
        }
    }

    val fileResult = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        fileResult.value = it

        it?.let {
            onEvent(MainEvent.OnDocumentLoaded(it))
        }
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
                conversationsRefreshing = uiState.conversationsRefreshing,
                conversationsLoading = uiState.conversationsLoading,
                selectedConversation = uiState.selectedConversation,
                conversations = uiState.conversations.filter { it.title.contains(uiState.conversationQuery, ignoreCase = true) },
                onConversationSelected = {
                    onEvent(MainEvent.OnConversationSelected(it))
                },
                onConversationsRefreshed = {
                    onEvent(MainEvent.OnConversationsRefreshed)
                },
                onLogout = { onEvent(MainEvent.OnLogout) },
                onQueryChanged = {
                    onEvent(MainEvent.OnConversationQueryChanged(it))
                },
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
                .background(MaterialTheme.colorScheme.background)
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
                    uiState.availableModel.forEachIndexed { index, option ->
                        SegmentedButton(
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primary,
                                activeContentColor = Color.White
                            ),
                            selected = option == uiState.selectedModel,
                            onClick = { onEvent(MainEvent.OnModelSelected(option)) },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = uiState.availableModel.size),
                        ) {
                            Text(text = option.name)
                        }
                    }

                }

                SquaredButton(
                    backgroundColor = Color.Transparent,
                    color = MaterialTheme.colorScheme.primary,
                    resource = R.drawable.add_chat,
                    onClick = {

                    })
            }

            if (uiState.messagesLoading){
                Loader(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    message = stringResource(id = R.string.loading_characters)
                )
            }
            else if (uiState.messages.isNotEmpty()){
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
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background),
                                text = date.toDateLabel(),
                            )
                        }

                        items(messages){ message ->
                            when(message.sender){
                                MessageSender.USER ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 12.dp, start = 48.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        MessageItem(content = message.content, date = message.date, isUser = true, model = message.model)
                                    }
                                MessageSender.AI ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 12.dp, end = 24.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        MessageItem(content = message.content, date = message.date, isUser = false, model = message.model)
                                    }
                            }
                        }

                        if (uiState.typing){
                            item {
                                LaunchedEffect(Unit){
                                    lazyListState.scrollToItem(uiState.messages.flatMap { it.value }.size + uiState.messages.size)
                                }

                                TypingMessage()
                            }
                        }
                    }

                }
            }
            else {
                EmptyList(modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .padding(16.dp),
            ) {
                Column {

                    LazyRow {
                        uiState.documents.forEach { uri, document ->
                            item {
                                PdfThumbnail(
                                    uri = uri,
                                    document = document,
                                    onLongClick = {
                                        onEvent(MainEvent.OnDocumentDeleted(uri))
                                    }
                                )
                            }
                        }
                    }

                    Row(
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
                            onTextChange = { onEvent(MainEvent.OnMessageChanged(it)) },
                            singleLine = false,
                            maxLines = 4)


                        SquaredButton(
                            resource = R.drawable.send,
                            onClick = {
                                onEvent(MainEvent.OnSendMessage(uiState.message))
                            })
                    }
                }

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
        fr.group5.magellangpt.domain.models.Message(id = 1, content = "Hello there", sender = MessageSender.USER, date = Date(), model = "gpt3"),
        fr.group5.magellangpt.domain.models.Message(id = 2, content = "Hello obi-wan", sender = MessageSender.AI, date = Date(), model = "gpt4"),
    )
    val uiState = MainUiState(messages = messages.groupBy { it.date })
    MainScreen(uiState = uiState, onEvent = {})
}