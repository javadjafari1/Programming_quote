package ir.partsoftware.programmingquote.features.quotesist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.partsoftware.programmingquote.R

@Composable
fun QuotesListScreen(
    name: String,
    onQuoteClicked: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
                title = {
                    Text(
                        text = name,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle1,
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO open detail dialog*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "info icon"
                        )
                    }
                },
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(34.dp),
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "author's image"
                    )
                }
            )
        }
    ) { paddingValues ->
        ScreenContent(
            modifier = Modifier.padding(paddingValues),
            onQuoteClicked = onQuoteClicked
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    onQuoteClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(34) {
            QuoteItem(
                text = " $it women can't make a baby in one month.",
                onClicked = {
                    onQuoteClicked(it)
                }
            )
        }
    }
}

@Composable
private fun QuoteItem(
    text: String,
    onClicked: () -> Unit
) {
    Text(
        modifier = Modifier
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
