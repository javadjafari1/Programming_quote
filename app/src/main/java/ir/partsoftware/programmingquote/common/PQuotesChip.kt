package ir.partsoftware.programmingquote.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

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
