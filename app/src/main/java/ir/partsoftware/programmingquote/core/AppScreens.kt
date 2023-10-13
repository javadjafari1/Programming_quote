package ir.partsoftware.programmingquote.core

sealed class AppScreens(val route: String) {

    data object AuthorsList : AppScreens("authors-list")

    data object QuotesList : AppScreens("quotes-list?authorId={authorId}&authorName={authorName}") {
        fun createRoute(authorId: String, authorName: String): String {
            return "quotes-list?authorId=$authorId&authorName=$authorName"
        }
    }

    data object Search : AppScreens("search")

    data object Quote : AppScreens("quote/{id}/{authorName}") {
        fun createRoute(id: String, authorName: String): String {
            return "quote/$id/$authorName"
        }
    }
}
