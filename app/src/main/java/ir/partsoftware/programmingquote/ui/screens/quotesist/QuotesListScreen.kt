package ir.partsoftware.programmingquote.ui.screens.quotesist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.network.quote.Quote
import ir.partsoftware.programmingquote.ui.common.PQuoteAppBar
import ir.partsoftware.programmingquote.ui.common.QuoteItem
import ir.partsoftware.programmingquote.ui.common.Result
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

@Composable
fun QuotesListScreen(
    authorId: String,
    authorName: String,
    onQuoteClicked: (String) -> Unit,
    viewModel: QuotesListViewModel = hiltViewModel()
) {
    var isFullImageShowing by remember { mutableStateOf(false) }
    var isInfoDialogShowing by remember { mutableStateOf(false) }

    val quoteResult by viewModel.quoteResult.collectAsState()
    val author by viewModel.author.collectAsState()
    val quotes by viewModel.quotes.collectAsState()

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(quoteResult) {
        if (quoteResult is Result.Error) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                (quoteResult as Result.Error).message,
                actionLabel = "retry",
                duration = SnackbarDuration.Indefinite
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.getAuthorQuote()
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            PQuoteAppBar(
                title = {
                    Text(
                        text = authorName,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle1,
                    )
                },
                actions = {
                    if (!author?.extract.isNullOrBlank()) {
                        IconButton(onClick = { isInfoDialogShowing = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "info icon"
                            )
                        }
                    }
                },
                navigationIcon = {
                    AsyncImage(
                        model = author?.image.orEmpty(),
                        contentDescription = "author's image",
                        error = painterResource(R.drawable.ic_profile),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable { isFullImageShowing = true },
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
                modifier = Modifier
                    .padding(paddingValues)
                    .align(Alignment.TopCenter),
                onQuoteClicked = onQuoteClicked,
                quoteResult = quoteResult,
                quotes = quotes
            )
            AnimatedVisibility(
                visible = isFullImageShowing,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { isFullImageShowing = false }
                        )
                ) {
                    AsyncImage(
                        model = author?.image.orEmpty(),
                        error = painterResource(R.drawable.ic_profile),
                        contentDescription = "author's image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(CircleShape),
                    )
                }
            }
            if (isInfoDialogShowing) {
                val extract = author?.extract
                if (!extract.isNullOrBlank()) {
                    AuthorDetailDialog(
                        name = authorName,
                        about = extract,
                        onDismiss = { isInfoDialogShowing = false }
                    )
                } else {
                    isInfoDialogShowing = false
                }
            }
        }
    }
}

@Composable
private fun AuthorDetailDialog(
    name: String,
    about: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(all = 24.dp)
                .verticalScroll(rememberScrollState())

        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = about,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    onQuoteClicked: (String) -> Unit,
    quoteResult: Result,
    quotes: List<Quote>
) {
    if (quoteResult is Result.Loading) {
        LinearProgressIndicator(
            modifier = modifier.fillMaxWidth()
        )
    }
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(quotes) { quote ->
            QuoteItem(
                text = quote.text,
                onClicked = {
                    onQuoteClicked(quote.id)
                }
            )
        }
    }
}

@Preview
@Composable
fun QuoteListScreenPreview() {
    ProgrammingQuoteTheme {
        QuotesListScreen(authorId = "asdklav45", authorName = "Javad jafari", onQuoteClicked = {})
    }
}

@Preview
@Composable
fun AuthorDetailDialogPreview() {
    ProgrammingQuoteTheme {
        AuthorDetailDialog(
            name = "javad jafari",
            about = LoremIpsum(30).values.joinToString()
        ) {}
    }
}