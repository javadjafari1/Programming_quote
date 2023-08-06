package ir.partsoftware.programmingquote.core

sealed class AppScreens(val route: String) {

    object AuthorsList : AppScreens("authors-list")

    object QuotesList : AppScreens("quotes-list/{id}") {
        fun createRoute(id: String): String {
            return "quotes-list/$id"
        }
    }

    object Search : AppScreens("search")

    object Quote : AppScreens("quote/{id}") {
        fun createRoute(id: Int): String {
            return "quote/$id"
        }
    }
}
