package ir.partsoftware.programmingquote.features.authorslist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AuthorsListScreen(
    onAuthorClicked: (Int) -> Unit,
    openSearch: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        content = {
            ScreenContent(
                openSearch = openSearch,
                onAuthorClicked = onAuthorClicked,
                generateRandom = {
                    scope.launch {
                        bottomSheetState.show()
                    }
                }
            )
        },
        sheetContent = {
            RandomQuote(
                name = "Jeff Sickel",
                quote = "Nine women can't make a baby in one month."
            )
        },
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        sheetElevation = 4.dp,

        )
}

@Composable
private fun ScreenContent(
    openSearch: () -> Unit,
    onAuthorClicked: (Int) -> Unit,
    generateRandom: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.app_name_short),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(
                        onClick = openSearch
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search-icon"
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "App-logo",
                        tint = MaterialTheme.colors.primary
                    )
                },
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
        },
        floatingActionButton = {
            Button(onClick = generateRandom) {
                Text(
                    text = stringResource(R.string.label_generate_random),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(30) {
                AuthorItem(
                    authorName = "$it. Edsger W. Dijkstra",
                    quotesCount = 24 + it,
                    onItemClick = {
                        /*it represent as Id*/
                        onAuthorClicked(it)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AuthorItem(
    authorName: String,
    quotesCount: Int,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
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
        // TODO replace this Image composable with Coil
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(75.dp),
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "author image"
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Text(
                modifier = Modifier.basicMarquee(),
                text = authorName,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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

@Preview
@Composable
fun AuthorsListPreview() {
    ProgrammingQuoteTheme {
        AuthorsListScreen(onAuthorClicked = {}, openSearch = {})
    }
}

@Composable
private fun RandomQuote(
    name: String,
    quote: String
) {
    Column(
        Modifier
            .padding(
                vertical = 32.dp,
                horizontal = 24.dp
            )
    ) {
        Text(
            text = stringResource(R.string.label_quote_author_once_said, name),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.body1,
        )

        Spacer(modifier = Modifier.size(32.dp))

        Text(
            text = quote,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}
