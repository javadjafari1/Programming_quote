package ir.partsoftware.programmingquote.ui.screens.search

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.core.SearchType
import ir.partsoftware.programmingquote.ui.common.AuthorItem
import ir.partsoftware.programmingquote.ui.common.PQuotesChip
import ir.partsoftware.programmingquote.ui.common.QuoteItem
import ir.partsoftware.programmingquote.ui.common.Result
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SearchScreen(
    onQuoteClicked: (id: String, authorName: String) -> Unit,
    onAuthorClicked: (id: String, name: String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchText by viewModel.query.collectAsState()
    val pagerState = rememberPagerState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val authors by viewModel.authors.collectAsState()
    val quotes by viewModel.quotes.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState(Result.Idle)

    LaunchedEffect(Unit) {
        viewModel.searchResult.onEach { searchResult ->
            if (searchResult is Result.Loading) {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            }
            if (searchResult is Result.Error) {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    searchResult.message,
                    actionLabel = context.getString(R.string.label_retry),
                    duration = SnackbarDuration.Indefinite
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.search()
                }
            }
        }.launchIn(this)
    }

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                value = searchText,
                onValueChange = { text -> viewModel.updateQuery(text) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    unfocusedBorderColor = MaterialTheme.colors.surface,
                    focusedBorderColor = MaterialTheme.colors.primary,
                ),
                shape = MaterialTheme.shapes.large,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                textStyle = MaterialTheme.typography.subtitle1,
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.search()
                    }
                )
            )

            Row(Modifier.padding(horizontal = 16.dp)) {
                PQuotesChip(
                    selected = pagerState.currentPage == SearchType.Programmer.ordinal,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(SearchType.Programmer.ordinal)
                        }
                    },
                ) {
                    Text(
                        text = stringResource(R.string.label_programmer),
                        style = MaterialTheme.typography.button
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                PQuotesChip(
                    selected = pagerState.currentPage == SearchType.Quote.ordinal,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(SearchType.Quote.ordinal)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.label_quote),
                        style = MaterialTheme.typography.button
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                pageCount = SearchType.values().size
            ) { page ->
                if (searchResult is Result.Loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else {
                    val noResult = hasNoResult(
                        items = if (page == SearchType.Programmer.ordinal) authors else quotes,
                        searchText = searchText,
                        searchResult = searchResult
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = if (noResult) {
                            Arrangement.Center
                        } else {
                            Arrangement.spacedBy(16.dp)
                        }
                    ) {
                        if (page == SearchType.Programmer.ordinal) {
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
                        } else if (page == SearchType.Quote.ordinal) {
                            items(quotes) { item ->
                                QuoteItem(
                                    text = item.quoteResponse.text,
                                    authorName = item.authorResponse.name,
                                    onClicked = {
                                        onQuoteClicked(item.quoteResponse.id, item.authorResponse.name)
                                    }
                                )
                            }
                        }

                        if (noResult) {
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "No Item Found",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun hasNoResult(
    items: List<Any>,
    searchText: String,
    searchResult: Result
) = items.isEmpty() && searchText.isNotEmpty() && searchResult is Result.Success

@Preview
@Composable
fun SearchScreenPreview() {
    ProgrammingQuoteTheme {
        SearchScreen(onQuoteClicked = { _, _ -> }, onAuthorClicked = { _, _ -> })
    }
}