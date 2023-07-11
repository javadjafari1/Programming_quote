package ir.partsoftware.programmingquote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.partsoftware.programmingquote.core.AppScreens
import ir.partsoftware.programmingquote.features.authorslist.AuthorsListScreen
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgrammingQuoteTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AppScreens.AuthorsList.route
                ) {
                    mainNavGraph(navController)
                }
            }
        }
    }
}

private fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    composable(route = AppScreens.AuthorsList.route) {
        AuthorsListScreen(
            onAuthorClicked = {/*TODO Open Quotes list with id*/ },
            openSearch = {/*TODO Navigate to Search*/ }
        )
    }
}