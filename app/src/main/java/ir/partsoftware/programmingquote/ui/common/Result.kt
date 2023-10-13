package ir.partsoftware.programmingquote.ui.common

sealed class Result {
    data object Idle : Result()
    data object Loading : Result()
    data class Error(val message: String) : Result()
    data object Success : Result()
}
