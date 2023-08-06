package ir.partsoftware.programmingquote.ui.screens.quotesist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.network.author.Author
import ir.partsoftware.programmingquote.network.quote.Quote
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesListViewModel @Inject constructor(
    private val quoteApi: QuoteApi,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _quoteResult = MutableStateFlow<Result>(Result.Idle)
    val quoteResult: StateFlow<Result> = _quoteResult.asStateFlow()

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _author = MutableStateFlow<Author?>(null)
    val author: StateFlow<Author?> = _author.asStateFlow()

    private val authorId: String
        get() = savedStateHandle.get<String>("authorId").orEmpty()

    init {
        getAuthorQuote()
    }

    fun getAuthorQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            _quoteResult.value = Result.Loading

            try {
                val response = quoteApi.getAuthorQuotes(authorId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _author.value = body.author
                        _quotes.value = body.quotes
                        _quoteResult.value = Result.Success
                    } else {
                        _quoteResult.value = Result.Error("whoops body was empty")
                    }
                } else {
                    _quoteResult.value = Result.Error("whoops: got ${response.code()} code!")
                }
            } catch (t: Throwable) {
                _quoteResult.value = Result.Error("whoops: ${t.message}")
            }
        }
    }
}