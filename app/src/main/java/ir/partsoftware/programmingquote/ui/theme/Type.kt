package ir.partsoftware.programmingquote.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.partsoftware.programmingquote.R

internal val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.firacode_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 55.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.firacode_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.firacode_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontFamily = FontFamily(Font(R.font.firacode_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    overline = TextStyle(
        fontFamily = FontFamily(Font(R.font.firacode_light)),
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    )
)