package ir.partsoftware.programmingquote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.partsoftware.programmingquote.core.AppScreens
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            ProgrammingQuoteTheme {
                val backgroundColor = MaterialTheme.colors.background
                DisposableEffect(Unit) {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.light(
                            scrim = backgroundColor.toArgb(),
                            darkScrim = Color.Black.copy(alpha = 0.3f).toArgb()
                        ),
                        navigationBarStyle = SystemBarStyle.light(
                            scrim = backgroundColor.toArgb(),
                            darkScrim = Color.Black.copy(alpha = 0.3f).toArgb()
                        )
                    )
                    onDispose {}
                }
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding()
                        .background(MaterialTheme.colors.background),
                    navController = navController,
                    startDestination = AppScreens.AuthorsList.route
                ) {
                    mainNavGraph(navController)
                }
            }
        }
    }
}
