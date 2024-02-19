package fr.group5.magellangpt.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.thomasbernard03.composents.TextField
import fr.thomasbernard03.composents.buttons.SquaredButton

@Composable
fun MainScreen(uiState: MainUiState, onEvent: (MainEvent) -> Unit){
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.weight(1f))
        

        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
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