package ir.partsoftware.programmingquote.ui.screens.quotelist

import android.widget.TextView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.database.quote.QuoteEntity
import ir.partsoftware.programmingquote.ui.common.PQuoteAppBar
import ir.partsoftware.programmingquote.ui.common.QuoteItem
import ir.partsoftware.programmingquote.ui.common.Result
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuotesListScreen(
    authorId: String,
    authorName: String,
    onQuoteClicked: (String) -> Unit,
    viewModel: QuotesListViewModel = hiltViewModel()
) {
    var isFullImageShowing by rememberSaveable { mutableStateOf(false) }
    var isInfoDialogShowing by rememberSaveable { mutableStateOf(false) }

    val quoteResult by viewModel.quoteResult.collectAsState(Result.Idle)
    val author by viewModel.author.collectAsState()
    val quotes by viewModel.quotes.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    BackHandler(isFullImageShowing) {
        isFullImageShowing = false
    }

    LaunchedEffect(Unit) {
        viewModel.quoteResult.collectLatest { quoteResult ->
            if (quoteResult is Result.Error) {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    quoteResult.message,
                    actionLabel = context.getString(R.string.label_retry),
                    duration = if (quotes.isEmpty()) {
                        SnackbarDuration.Indefinite
                    } else {
                        SnackbarDuration.Long
                    }
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.fetchAuthorQuotes(authorId)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            PQuoteAppBar(
                title = {
                    Text(text = authorName)
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
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(MaterialTheme.shapes.large),
                        model = author?.image.orEmpty(),
                        error = painterResource(R.drawable.ic_profile),
                        contentDescription = "$authorName's image",
                        contentScale = ContentScale.Fit,
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = name,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.size(16.dp))
            AndroidView(
                modifier = Modifier.padding(bottom = 24.dp),
                factory = { context -> TextView(context) },
                update = {
                    it.text = HtmlCompat.fromHtml(about, HtmlCompat.FROM_HTML_MODE_COMPACT)
                }
            )
        }
    }
}

@Composable
private fun ScreenContent(
    quoteResult: Result,
    quotes: List<QuoteEntity>,
    modifier: Modifier = Modifier,
    onQuoteClicked: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (quoteResult is Result.Loading) {
            LinearProgressIndicator(
                modifier = modifier.fillMaxWidth()
            )
        }
        LazyColumn(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
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
}

@Preview
@Composable
fun QuoteListScreenPreview() {
    ProgrammingQuoteTheme {
        QuotesListScreen(
            authorId = "asdklav45",
            authorName = "Javad jafari",
            onQuoteClicked = {}
        )
    }
}

@Preview
@Composable
fun AuthorDetailDialogPreview() {
    ProgrammingQuoteTheme {
        AuthorDetailDialog(
            name = "javad jafari",
            about = LoremIpsum(30).values.joinToString(),
            onDismiss = {}
        )
    }
}