package ir.partsoftware.programmingquote.features.quotesist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.partsoftware.programmingquote.R

@Composable
fun QuotesListScreen(
    name: String,
    onQuoteClicked: (Int) -> Unit
) {
    var isFullImageDisplayed by remember { mutableStateOf(false) }

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
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable { isFullImageDisplayed = true },
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "author's image"
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ScreenContent(
                modifier = Modifier.padding(paddingValues),
                onQuoteClicked = onQuoteClicked
            )
            AnimatedVisibility(
                visible = isFullImageDisplayed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { isFullImageDisplayed = false }
                        )
                )
                {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(CircleShape),
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "author's image",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
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
