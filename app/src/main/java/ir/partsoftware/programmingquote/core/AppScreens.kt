package ir.partsoftware.programmingquote.core

sealed class AppScreens(val route: String) {

    object AuthorsList : AppScreens("authors-list")

    object QuotesList : AppScreens("quotes-list/{id}") {
        fun createRoute(id: Int): String {
            return "quotes-list/$id"
        }
    }
}
