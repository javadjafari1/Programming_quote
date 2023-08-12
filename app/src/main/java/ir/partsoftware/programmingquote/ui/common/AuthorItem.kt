package ir.partsoftware.programmingquote.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuthorItem(
    authorName: String,
    quotesCount: Int?,
    authorImage: String?,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.background)
            .clickable(onClick = onItemClick)
            .padding(all = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = authorImage,
            contentDescription = "$authorName's image",
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_profile),
            modifier = Modifier
                .clip(CircleShape)
                .size(75.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Text(
                modifier = Modifier.basicMarquee(),
                text = authorName,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
            )
            if (quotesCount != null) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = stringResource(R.string.quotes_count, quotesCount),
                    style = MaterialTheme.typography.overline,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
fun AuthorItemPreview() {
    ProgrammingQuoteTheme {
        AuthorItem(
            authorName = "javad jafari",
            quotesCount = 34,
            authorImage = null,
            onItemClick = {}
        )

    }
}