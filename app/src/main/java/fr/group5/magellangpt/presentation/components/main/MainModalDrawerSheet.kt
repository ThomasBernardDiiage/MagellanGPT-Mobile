package fr.group5.magellangpt.presentation.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.presentation.components.Loader
import fr.group5.magellangpt.presentation.components.OutlinedButton
import fr.group5.magellangpt.presentation.main.MainEvent
import fr.group5.magellangpt.presentation.theme.Primary
import fr.group5.magellangpt.presentation.theme.Secondary
import fr.thomasbernard03.composents.TextField
import fr.thomasbernard03.composents.buttons.SquaredButton

@Composable
fun MainModalDrawerSheet(
    firstname : String,
    lastname : String,
    email : String,
    query : String,

    conversationsLoading : Boolean = false,
    conversations : List<Conversation> = emptyList(),
    selectedConversation: Conversation? = null,

    onConversationSelected : (Conversation) -> Unit = {},
    onQueryChanged : (String) -> Unit = {},
    onLogout : () -> Unit = {},
    onClose : () -> Unit = {}
){
    val uriHandler = LocalUriHandler.current

    ModalDrawerSheet {
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

            Text(
                text = stringResource(id = R.string.conversations),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp))

            HorizontalDivider()

            if (conversationsLoading){
                Loader(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    message = stringResource(id = R.string.loading_conversations)
                )
            }
            else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f),
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
            }



            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    val uri = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                    uriHandler.openUri(uri)
                }
            ){
                Text(text = stringResource(id = R.string.quota))
                
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.flask),
                    contentDescription = "Flash")
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    val uri = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                    uriHandler.openUri(uri)
                }
            ) {
                Text(text = stringResource(id = R.string.more_informations))

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "Informations")
            }

            HorizontalDivider()


            Row(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "$firstname ${lastname.uppercase()}")
                    Text(text = email)
                }
                SquaredButton(
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    resource = R.drawable.logout,
                    onClick = onLogout)
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