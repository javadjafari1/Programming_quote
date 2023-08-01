package ir.partsoftware.programmingquote.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

@Composable
fun QuoteItem(
    text: String,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    Text(
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
            .padding(all = 24.dp),
        text = text,
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.primary
    )
}

@Preview
@Composable
fun QuoteItemPreview() {
    ProgrammingQuoteTheme {
        QuoteItem(text = "hey") {

        }
    }
}