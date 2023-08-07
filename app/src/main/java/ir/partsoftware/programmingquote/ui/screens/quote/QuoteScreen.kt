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


@Composable
fun QuoteScreen(
    id: String,
    viewModel: QuoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()

    val quoteResult by viewModel.quoteResult.collectAsState()
    val quote by viewModel.quote.collectAsState()

    LaunchedEffect(quoteResult) {
        if (quoteResult is Result.Error) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                (quoteResult as Result.Error).message,
                actionLabel = "retry",
                duration = SnackbarDuration.Indefinite
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.getQuote(id)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            quote?.let { quote ->
                PQuoteAppBar {
                    Text(
                        text = quote.author.name,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    ) {
        ScreenContent(
            modifier = Modifier.padding(it),
            onShareClicked = {
                context.shareText(quote?.quote?.text.orEmpty())
            },
            onOpenWikipediaClicked = {
                context.openUrl(quote?.author?.infoUrl.orEmpty())
            },
            quote = quote?.quote?.text,
            quoteResult = quoteResult,
            showWikiLink = !quote?.author?.infoUrl.isNullOrBlank(),
            showShare = quote != null
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
    showWikiLink: Boolean,
    showShare: Boolean
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (quoteResult is Result.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        AutoResizeText(
            modifier = Modifier
                .weight(1f),
            text = quote.orEmpty(),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (showShare || showWikiLink) {
            Row(modifier = Modifier.fillMaxWidth()) {
                if (showShare) {
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
                }

                if (showWikiLink) {
                    if (showShare)
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
}

@Preview
@Composable
fun QuoteScreenPreview() {
    ProgrammingQuoteTheme {
        QuoteScreen(id = "Javad jafari")
    }
}