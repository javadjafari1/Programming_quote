package ir.partsoftware.programmingquote.ui.screens.authorslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.network.author.Author
import ir.partsoftware.programmingquote.network.author.AuthorApi
import ir.partsoftware.programmingquote.network.common.safeApi
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import ir.partsoftware.programmingquote.network.quote.QuoteResponse
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
    val authorResult: SharedFlow<Result> = _authorResult.asSharedFlow()

    private val _randomResult = MutableSharedFlow<Result>()
    val randomResult: SharedFlow<Result> = _randomResult.asSharedFlow()

    private val _authors = MutableStateFlow<List<Author>>(emptyList())
    val authors: StateFlow<List<Author>> = _authors.asStateFlow()

    private val _randomQuote = MutableStateFlow<QuoteResponse?>(null)
    val randomQuote: StateFlow<QuoteResponse?> = _randomQuote.asStateFlow()

    init {
        getAuthors()
    }

    fun getAuthors() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { authorApi.getAuthors() },
                onDataReady = { _authors.value = it }
            ).collect(_authorResult)
        }
    }

    fun getRandomQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { quoteApi.getRandomQuote() },
                onDataReady = { _randomQuote.value = it }
            ).collect(_randomResult)
        }
    }
}