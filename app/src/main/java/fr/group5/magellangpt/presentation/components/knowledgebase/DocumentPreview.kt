package fr.group5.magellangpt.presentation.components.knowledgebase

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pspdfkit.document.PdfDocument

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DocumentPreview(
    modifier : Modifier = Modifier,
    uri : Uri,
    document: PdfDocument,
    onLongClick : () -> Unit
) {
    val context = LocalContext.current

    // Since this can be a costly operation, we wanna memoize the
    // bitmap to prevent recalculating it every time we recompose.
    val thumbnailImage = remember(document) {
        val pageImageSize = document.getPageSize(0).toRect()
        document.renderPageToBitmap(context, 0, pageImageSize.width().toInt(), pageImageSize.height().toInt()).asImageBitmap()
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        modifier = modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/pdf")
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }
                    context.startActivity(intent)
                },
                onLongClick = {
                    onLongClick()
                }
            )
//            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary), RoundedCornerShape(4.dp))
    ) {
        Column {
            Image(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                bitmap = thumbnailImage,
                contentScale = ContentScale.FillWidth,
                contentDescription = "Preview for the document ${document.title}",
            )

            Text(
                textAlign = TextAlign.Center,
                text = document.title ?: "",
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}