package fr.group5.magellangpt.presentation.components.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.PreviewComponents


@Composable
fun SettingItem(
    modifier : Modifier = Modifier,
    color : Color = Color.DarkGray,
    @DrawableRes icon : Int,
    @StringRes title : Int,
    subtitle : String,
    onClick : () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 12.dp)
    ) {
        Icon(
            modifier = Modifier.size(36.dp).padding(end = 16.dp),
            tint = color,
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = title))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = stringResource(id = title),
                color = color)

            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = subtitle,
                color = color.copy(0.7f))
        }
    }
}

@Composable
fun SettingItem(
    modifier : Modifier = Modifier,
    color : Color = Color.DarkGray,
    @DrawableRes icon : Int,
    @StringRes title : Int,
    onClick : () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 12.dp)
    ) {
        Icon(
            modifier = Modifier.size(36.dp).padding(end = 16.dp),
            tint = color,
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = title))

        Text(
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            modifier = Modifier.weight(1f),
            text = stringResource(id = title)
        )
    }
}

@Composable
@Preview
private fun SettingItemPreview() = PreviewComponents {
    SettingItem(
        modifier = Modifier.fillMaxWidth(),
        icon = R.drawable.logout,
        title = R.string.logout,
        subtitle = "Version 1.0.0",
        onClick = {}
    )

    SettingItem(
        modifier = Modifier.fillMaxWidth(),
        icon = R.drawable.logout,
        title = R.string.logout,
        onClick = {}
    )
}