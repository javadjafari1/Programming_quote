package ir.partsoftware.programmingquote.core

sealed class AppScreens(val route: String) {

    object AuthorsList : AppScreens("authors-list")
}
