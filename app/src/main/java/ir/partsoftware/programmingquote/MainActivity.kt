package ir.partsoftware.programmingquote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.partsoftware.programmingquote.core.AppScreens
import ir.partsoftware.programmingquote.features.authorslist.AuthorsListScreen
import ir.partsoftware.programmingquote.features.quotesist.QuotesListScreen
import ir.partsoftware.programmingquote.features.search.SearchScreen
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
                type = NavType.IntType
                nullable = false
            }
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id")
            ?: throw IllegalStateException("id was null")
        QuotesListScreen(
            name = "$id. Edsger W. Dijkstra",
            onQuoteClicked = {/*TODO open quoteScreen with id*/ }
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
            onQuoteClicked = {/*TODO open Quote screen*/ }
        )
    }
}