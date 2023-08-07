package ir.partsoftware.programmingquote.ui.screens.quotesist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.network.author.Author
import ir.partsoftware.programmingquote.network.common.safeApi
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
            safeApi(
                call = { quoteApi.getAuthorQuotes(authorId) },
                onDataReady = {
                    _author.value = it.author
                    _quotes.value = it.quotes
                }
            ).collect(_quoteResult)
        }
    }
}