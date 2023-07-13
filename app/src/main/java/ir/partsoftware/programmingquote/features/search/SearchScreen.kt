package ir.partsoftware.programmingquote.features.search

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.common.AuthorItem
import ir.partsoftware.programmingquote.common.PQuotesChip
import ir.partsoftware.programmingquote.common.QuoteItem
import ir.partsoftware.programmingquote.core.SearchType
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SearchScreen(
    onQuoteClicked: (Int) -> Unit,
    onAuthorClicked: (Int) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Scaffold { paddingValues ->
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
                onValueChange = { text -> searchText = text },
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
                maxLines = 2
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
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (page == SearchType.Programmer.ordinal) {
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
                    } else if (page == SearchType.Quote.ordinal) {
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
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    ProgrammingQuoteTheme {
        SearchScreen(onQuoteClicked = {}, onAuthorClicked = {})
    }
}