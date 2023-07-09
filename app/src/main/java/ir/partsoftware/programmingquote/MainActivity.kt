package ir.partsoftware.programmingquote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgrammingQuoteTheme {

            }
        }
    }
}
