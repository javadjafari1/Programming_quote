package ir.partsoftware.programmingquote.ui.screens.authorslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.network.author.Author
import ir.partsoftware.programmingquote.network.author.AuthorApi
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import ir.partsoftware.programmingquote.network.quote.RandomQuoteResponse
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorsListViewModel @Inject constructor(
    private val authorApi: AuthorApi,
    private val quoteApi: QuoteApi
) : ViewModel() {

    private val _authorResult = MutableStateFlow<Result>(Result.Idle)
    val authorResult: StateFlow<Result> = _authorResult.asStateFlow()

    private val _randomResult = MutableSharedFlow<Result>()
    val randomResult: SharedFlow<Result> = _randomResult.asSharedFlow()

    private val _authors = MutableStateFlow<List<Author>>(emptyList())
    val authors: StateFlow<List<Author>> = _authors.asStateFlow()

    private val _randomQuote = MutableStateFlow<RandomQuoteResponse?>(null)
    val randomQuote: StateFlow<RandomQuoteResponse?> = _randomQuote.asStateFlow()

    init {
        getAuthors()
    }

    fun getAuthors() {
        viewModelScope.launch(Dispatchers.IO) {
            _authorResult.value = Result.Loading

            try {
                val response = authorApi.getAuthors()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _authors.value = body
                        _authorResult.value = Result.Success
                    } else {
                        _authorResult.value = Result.Error("whoops body was empty")
                    }
                } else {
                    _authorResult.value = Result.Error("whoops: got ${response.code()} code!")
                }
            } catch (t: Throwable) {
                _authorResult.value = Result.Error("whoops: ${t.message}")
            }
        }
    }

    fun getRandomQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            _randomResult.emit(Result.Loading)

            try {
                val response = quoteApi.getRandomQuote()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _randomQuote.value = body
                        _randomResult.emit(Result.Success)
                    } else {
                        _randomResult.emit(Result.Error("whoops body was empty"))
                    }
                } else {
                    _randomResult.emit(Result.Error("whoops: got ${response.code()} code!"))
                }
            } catch (t: Throwable) {
                _randomResult.emit(Result.Error("whoops: ${t.message}"))
            }
        }
    }
}