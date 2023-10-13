package ir.partsoftware.programmingquote.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SelectableChipColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PQuotesChip(
    selected: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colors.primary),
    colors: SelectableChipColors = ChipDefaults.filterChipColors(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onSurface,
        selectedBackgroundColor = MaterialTheme.colors.surface,
    ),
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        shape = shape,
        colors = colors,
        border = border,
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PQuoteChipPreview() {
    ProgrammingQuoteTheme {
        Column {
            PQuotesChip(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "Programmer")
            }
            PQuotesChip(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "Quote")
            }
        }
    }
}
