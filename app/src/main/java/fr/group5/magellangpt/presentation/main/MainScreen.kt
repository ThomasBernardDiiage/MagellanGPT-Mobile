package fr.group5.magellangpt.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainScreen(uiState: MainUiState, onEvent: (MainEvent) -> Unit){
    Column {
        
        Text(text = uiState.query)

    }
}