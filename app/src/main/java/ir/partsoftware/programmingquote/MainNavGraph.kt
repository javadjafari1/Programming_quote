package ir.partsoftware.programmingquote

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.partsoftware.programmingquote.core.AppScreens
import ir.partsoftware.programmingquote.ui.screens.authorslist.AuthorsListScreen
import ir.partsoftware.programmingquote.ui.screens.quote.QuoteScreen
import ir.partsoftware.programmingquote.ui.screens.quotelist.QuotesListScreen
import ir.partsoftware.programmingquote.ui.screens.search.SearchScreen

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    composable(route = AppScreens.AuthorsList.route) {
        AuthorsListScreen(
            onAuthorClicked = { id, name ->
                navController.navigate(
                    AppScreens.QuotesList.createRoute(id, name)
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
                defaultValue = ""
                nullable = false
            },
            navArgument("name") {
                type = NavType.StringType
                defaultValue = ""
                nullable = false
            }
        )
    ) { backStackEntry ->
        val authorId = backStackEntry.arguments?.getString("authorId")
            ?: throw IllegalStateException("authorId was null")
        val authorName = backStackEntry.arguments?.getString("authorName")
            ?: throw IllegalStateException("authorName was null")

        QuotesListScreen(
            authorId = authorId,
            authorName = authorName,
            onQuoteClicked = { quoteId ->
                navController.navigate(AppScreens.Quote.createRoute(quoteId, authorName))
            }
        )
    }
    composable(
        route = AppScreens.Search.route
    ) {
        SearchScreen(
            onAuthorClicked = { id, name ->
                navController.navigate(
                    AppScreens.QuotesList.createRoute(id, name)
                )
            },
            onQuoteClicked = { quoteId, authorName ->
                navController.navigate(AppScreens.Quote.createRoute(quoteId, authorName))
            }
        )
    }
    composable(
        route = AppScreens.Quote.route,
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
                nullable = false
            },
            navArgument("authorName") {
                type = NavType.StringType
                nullable = false
                defaultValue = ""
            }
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
            ?: throw IllegalStateException("id was null")
        val authorName = backStackEntry.arguments?.getString("authorName")
            ?: throw IllegalStateException("id was null")

        QuoteScreen(
            id = id,
            authorName = authorName
        )
    }
}