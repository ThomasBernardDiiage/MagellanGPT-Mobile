package fr.group5.magellangpt.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun PreviewComponents(content : @Composable (ColumnScope) -> Unit) {
    Surface {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            content(this)
        }
    }
}