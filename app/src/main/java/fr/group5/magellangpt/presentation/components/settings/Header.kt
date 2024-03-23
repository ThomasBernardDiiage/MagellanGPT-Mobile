package fr.group5.magellangpt.presentation.components.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun Header(
    modifier : Modifier = Modifier,
    @StringRes title : Int
) {
    Text(
        text = stringResource(id = title),
        modifier = modifier.padding(top = 12.dp)
    )
}