package fr.group5.magellangpt.presentation.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicatorDefaults
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import fr.group5.magellangpt.R
import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.presentation.components.Loader
import fr.group5.magellangpt.presentation.components.OutlinedButton
import fr.group5.magellangpt.presentation.components.TextField
import fr.thomasbernard03.composents.buttons.SquaredButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainModalDrawerSheet(
    firstname : String,
    lastname : String,
    email : String,
    query : String,

    conversationsLoading : Boolean = false,
    conversationsRefreshing : Boolean = false,
    conversations : List<Conversation> = emptyList(),
    selectedConversation: Conversation? = null,

    onConversationsRefreshed : () -> Unit = {},
    onConversationSelected : (Conversation) -> Unit = {},
    onQueryChanged : (String) -> Unit = {},
    onGoToSettings : () -> Unit = {},
    onClose : () -> Unit = {}
){
    val uriHandler = LocalUriHandler.current
    val refreshState = rememberPullRefreshState(conversationsRefreshing, {onConversationsRefreshed()})

    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.padding(vertical = 16.dp))

                Spacer(modifier = Modifier.weight(1f))

                SquaredButton(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    resource = R.drawable.close,
                    onClick = onClose)
            }


            TextField(
                text = query,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = stringResource(id = R.string.search_conversation),
                onTextChange = onQueryChanged,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "search")
                },
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                HorizontalDivider()

                if (conversationsLoading){
                    Loader(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        message = stringResource(id = R.string.loading_conversations)
                    )
                }
                else {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .pullRefresh(state = refreshState)
                    ){

                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(conversations) { conversation ->
                                ConversationItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    selected = selectedConversation == conversation,
                                    title = conversation.title
                                ) {
                                    onClose()

                                    if(selectedConversation != conversation)
                                        onConversationSelected(conversation)
                                }
                            }
                        }



                        PullRefreshIndicator(
                            colors = PullRefreshIndicatorDefaults.colors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            refreshing = conversationsRefreshing,
                            state = refreshState,
                            shadowElevation = 6.dp,
                            modifier = Modifier.align(Alignment.TopCenter))
                    }

                }
                HorizontalDivider()
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    onGoToSettings()
                }
            ){
                Text(text = stringResource(id = R.string.quota))

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    painter = painterResource(id = R.drawable.flask),
                    contentDescription = stringResource(id = R.string.quota))
            }


            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    onGoToSettings()
                }
            ){
                Text(text = stringResource(id = R.string.settings))

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = stringResource(id = R.string.settings))
            }
        }
    }
}

@Composable
@Preview
private fun MainModalDrawerSheetPreview() {
    MainModalDrawerSheet(
        firstname = "Thomas",
        lastname = "Bernard",
        email = "thomas.bernard@diiage.org",
        query = ""
    )
}