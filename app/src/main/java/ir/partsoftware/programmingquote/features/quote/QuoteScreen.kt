package ir.partsoftware.programmingquote.features.quote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ir.partsoftware.programmingquote.R
import ir.partsoftware.programmingquote.core.openUrl
import ir.partsoftware.programmingquote.core.shareText


@Composable
fun QuoteScreen(name: String) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    ) {
        ScreenContent(
            modifier = Modifier.padding(it),
            onShareClicked = {
                context.shareText("TODO")
            },
            onOpenWikipediaClicked = {
                context.openUrl("http://www.google.com")
            }
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    onShareClicked: () -> Unit,
    onOpenWikipediaClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = LoremIpsum(12).values.joinToString(),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h1
        )

        Spacer(modifier = Modifier.weight(1f))

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
