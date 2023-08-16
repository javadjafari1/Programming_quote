package ir.partsoftware.programmingquote.ui.screens.quote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.core.openUrl
import ir.partsoftware.programmingquote.core.shareText
import ir.partsoftware.programmingquote.ui.common.AutoResizeText
import ir.partsoftware.programmingquote.ui.common.PQuoteAppBar
import ir.partsoftware.programmingquote.ui.common.Result
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Composable
fun QuoteScreen(
    id: String,
    authorName: String,
    viewModel: QuoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()

    val quoteResult by viewModel.quoteResult.collectAsState(Result.Idle)
    val quoteWithAuthor by viewModel.quoteWithAuthor.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.quoteResult.onEach { quoteResult ->
            if (quoteResult is Result.Error) {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    quoteResult.message,
                    actionLabel = context.getString(R.string.label_retry),
                    duration = SnackbarDuration.Indefinite
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.fetchQuote(id)
                }
            }
        }.launchIn(this)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            PQuoteAppBar {
                Text(
                    text = authorName,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    ) {
        ScreenContent(
            modifier = Modifier.padding(it),
            onShareClicked = {
                context.shareText(quoteWithAuthor?.quote?.text.orEmpty())
            },
            onOpenWikipediaClicked = {
                context.openUrl(quoteWithAuthor?.author?.infoUrl.orEmpty())
            },
            quote = quoteWithAuthor?.quote?.text,
            quoteResult = quoteResult,
            showWikiLink = !quoteWithAuthor?.author?.infoUrl.isNullOrBlank()
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    onShareClicked: () -> Unit,
    onOpenWikipediaClicked: () -> Unit,
    quote: String?,
    quoteResult: Result,
    showWikiLink: Boolean
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (quote != null) {
            AutoResizeText(
                modifier = Modifier
                    .weight(1f),
                text = quote.orEmpty(),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onShareClicked
                ) {
                    Text(
                        text = stringResource(R.string.label_share),
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }

                if (showWikiLink) {
                    Spacer(modifier = Modifier.size(16.dp))

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onOpenWikipediaClicked
                    ) {
                        Text(
                            text = stringResource(R.string.label_open_wikipedia),
                            style = MaterialTheme.typography.button,
                            color = MaterialTheme.colors.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
    if (quoteResult is Result.Loading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun QuoteScreenPreview() {
    ProgrammingQuoteTheme {
        QuoteScreen(id = "4hgx2bpl4qyhql5", authorName = "Javad jafari")
    }
}