package ir.partsoftware.programmingquote.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PQuoteAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 0.dp
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        elevation = elevation,
        title = title,
        actions = actions,
        navigationIcon = navigationIcon
    )
}

@Composable
fun PQuoteAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        modifier = modifier,
        elevation = elevation,
        backgroundColor = backgroundColor,
        contentPadding = contentPadding,
        content = content
    )
}
