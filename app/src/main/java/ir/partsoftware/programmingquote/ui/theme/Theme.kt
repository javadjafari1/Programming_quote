package ir.partsoftware.programmingquote.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val lightColorPalette = lightColors(
    primary = Navy,
    onPrimary = White700,
    background = White500,
    onBackground = SemiBlack,
    surface = LightGray,
    onSurface = Navy
)

@Composable
fun ProgrammingQuoteTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = lightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}