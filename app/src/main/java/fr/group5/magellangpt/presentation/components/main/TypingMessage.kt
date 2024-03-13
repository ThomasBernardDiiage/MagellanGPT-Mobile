package fr.group5.magellangpt.presentation.components.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.presentation.theme.Secondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TypingMessage() {
    Card(
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomEnd =  8.dp,
            bottomStart = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Secondary
        ),
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            style = TextStyle(color = Color.White),
            text = "Typing...",)
    }
}