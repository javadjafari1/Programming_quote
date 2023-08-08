package ir.partsoftware.programmingquote.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

@Composable
fun QuoteItem(
    text: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    authorName: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.background)
            .clickable(onClick = onClicked)
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        if (authorName != null) {
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = authorName,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun QuoteItemPreview() {
    ProgrammingQuoteTheme {
        QuoteItem(text = "hey", onClicked = {})
    }
}