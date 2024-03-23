package fr.group5.magellangpt.presentation.components.knowledgebase

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.group5.magellangpt.R
import fr.group5.magellangpt.presentation.components.PreviewComponents

@Composable
fun KnowledgeBaseDescription(
    modifier : Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(end = 90.dp)
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.knowledge_base),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge)

            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.kesako),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge)

            Text(
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.knowledge_base_description))
        }

        Image(
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(200.dp).align(Alignment.TopEnd).offset(x = 50.dp),
            painter = painterResource(id = R.drawable.women),
            contentDescription = null)
    }
}


@Composable
@Preview
private fun KnowledgeBaseDescriptionPreview() = PreviewComponents {
    KnowledgeBaseDescription(modifier = Modifier.fillMaxWidth())
}