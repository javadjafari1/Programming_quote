package ir.partsoftware.programmingquote.ui.screens.authorslist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.database.author.AuthorEntity
import ir.partsoftware.programmingquote.ui.common.AuthorItem
import ir.partsoftware.programmingquote.ui.common.PQuoteAppBar
import ir.partsoftware.programmingquote.ui.common.Result
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AuthorsListScreen(
    onAuthorClicked: (id: String, name: String) -> Unit,
    openSearch: () -> Unit,
    viewModel: AuthorsListViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()

    val randomQuote by viewModel.randomQuote.collectAsState()
    val authors by viewModel.authors.collectAsState()
    val authorResult by viewModel.authorResult.collectAsState(Result.Idle)
    val randomResult by viewModel.randomResult.collectAsState(Result.Idle)

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(Unit) {
        launch {
            viewModel.randomResult.collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        scaffoldState.snackbarHostState.showSnackbar(result.message)
                    }

                    Result.Success -> {
                        bottomSheetState.show()
                    }

                    Result.Idle,
                    Result.Loading -> {
                    }

                }
            }
        }

        launch {
            viewModel.authorResult.collectLatest { authorResult ->
                if (authorResult is Result.Error) {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = authorResult.message,
                        actionLabel = context.getString(R.string.label_retry),
                        duration = if (authors.isNotEmpty()) {
                            SnackbarDuration.Long
                        } else {
                            SnackbarDuration.Indefinite
                        }
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.fetchAuthors()
                    }
                }
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.statusBarsPadding(),
        sheetState = bottomSheetState,
        content = {
            ScreenContent(
                openSearch = openSearch,
                onAuthorClicked = onAuthorClicked,
                generateRandom = {
                    viewModel.getRandomQuote()
                },
                scaffoldState = scaffoldState,
                randomResult = randomResult,
                authors = authors,
                authorResult = authorResult
            )
        },
        sheetContent = {
            randomQuote?.let {
                RandomQuote(
                    authorName = it.author.name,
                    quote = it.quote.text
                )
            }
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
    scaffoldState: ScaffoldState,
    authors: List<AuthorEntity>,
    authorResult: Result,
    randomResult: Result,
    openSearch: () -> Unit,
    generateRandom: () -> Unit,
    onAuthorClicked: (id: String, name: String) -> Unit,
) {
    val density = LocalDensity.current
    var fabHeight by rememberSaveable { mutableIntStateOf(0) }
    val fabHeightInDp by remember(fabHeight) {
        derivedStateOf { with(density) { fabHeight.toDp() + 32.dp } }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            PQuoteAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.app_name_short),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = openSearch) {
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
            )
        },
        floatingActionButton = {
            Button(
                modifier = Modifier.onGloballyPositioned {
                    fabHeight = it.size.height
                },
                onClick = generateRandom,
                enabled = randomResult !is Result.Loading,
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = MaterialTheme.colors.primary
                )
            ) {
                if (randomResult == Result.Loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(
                        text = stringResource(R.string.label_generate_random),
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (authorResult is Result.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = fabHeightInDp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(authors) { author ->
                    AuthorItem(
                        authorName = author.name,
                        quotesCount = author.quoteCount,
                        authorImage = author.image,
                        onItemClick = {
                            onAuthorClicked(author.id, author.name)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AuthorsListPreview() {
    ProgrammingQuoteTheme {
        AuthorsListScreen(
            onAuthorClicked = { _, _ -> },
            openSearch = {}
        )
    }
}

@Composable
private fun RandomQuote(
    authorName: String,
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
            text = stringResource(R.string.label_quote_author_once_said, authorName),
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

