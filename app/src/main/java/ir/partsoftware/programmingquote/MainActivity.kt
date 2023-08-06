package ir.partsoftware.programmingquote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ir.partsoftware.programmingquote.core.AppScreens
import ir.partsoftware.programmingquote.ui.screens.authorslist.AuthorsListScreen
import ir.partsoftware.programmingquote.ui.screens.quote.QuoteScreen
import ir.partsoftware.programmingquote.ui.screens.quotesist.QuotesListScreen
import ir.partsoftware.programmingquote.ui.screens.search.SearchScreen
import ir.partsoftware.programmingquote.ui.theme.ProgrammingQuoteTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
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
            onAuthorClicked = {
                navController.navigate(
                    AppScreens.QuotesList.createRoute(it)
                )
            },
            openSearch = { navController.navigate(AppScreens.Search.route) }
        )
    }
    composable(
        route = AppScreens.QuotesList.route,
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
            ?: throw IllegalStateException("id was null")
        QuotesListScreen(
            id = id,
            name = "$id. Edsger W. Dijkstra",
            onQuoteClicked = { quoteId ->
                navController.navigate(AppScreens.Quote.createRoute(quoteId))
            }
        )
    }
    composable(
        route = AppScreens.Search.route
    ) {
        SearchScreen(
            onAuthorClicked = {
                navController.navigate(
                    AppScreens.QuotesList.createRoute(it)
                )
            },
            onQuoteClicked = { quoteId ->
                navController.navigate(AppScreens.Quote.createRoute(quoteId))
            }
        )
    }
    composable(
        route = AppScreens.Quote.route,
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                nullable = false
            }
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id")
            ?: throw IllegalStateException("id was null")
        QuoteScreen(
            name = "$id Edsger W. Dijkstra"
        )
    }
}