package fr.group5.magellangpt.presentation.components.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ConversationItem(
    modifier : Modifier = Modifier,
    selected : Boolean,
    title : String,
    onClick : () -> Unit
) {
    Button(
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        contentPadding = PaddingValues(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color.Black.copy(alpha = 0.1f) else Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
        )
    }
}

@Composable
@Preview
private fun ConversationItemPreview() {
    ConversationItem(
        title = "Hello, World!",
        selected = false,
        onClick = {}
    )
}