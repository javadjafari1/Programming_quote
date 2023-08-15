package ir.partsoftware.programmingquote.ui.screens.quotesist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.database.author.AuthorDao
import ir.partsoftware.programmingquote.database.quote.QuoteDao
import ir.partsoftware.programmingquote.database.quote.QuoteEntity
import ir.partsoftware.programmingquote.network.author.Author
import ir.partsoftware.programmingquote.network.common.safeApi
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import ir.partsoftware.programmingquote.network.quote.toQuoteEntity
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesListViewModel @Inject constructor(
    private val quoteDao: QuoteDao,
    private val authorDao: AuthorDao,
    private val quoteApi: QuoteApi,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _quoteResult = MutableStateFlow<Result>(Result.Idle)
    val quoteResult: SharedFlow<Result> = _quoteResult.asSharedFlow()

    private val _quotes = MutableStateFlow<List<QuoteEntity>>(emptyList())
    val quotes: StateFlow<List<QuoteEntity>> = _quotes.asStateFlow()

    private val _author = MutableStateFlow<Author?>(null)
    val author: StateFlow<Author?> = _author.asStateFlow()

    private val authorId: String
        get() = savedStateHandle.get<String>("authorId").orEmpty()

    init {
        observeAuthorQuotes(authorId)
        fetchAuthorQuotes(authorId)
    }

    private fun observeAuthorQuotes(authorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authorDao.observeAuthorWithQuotes(authorId).collect {
                _quotes.emit(it.quoteEntities)
            }
        }
    }

    fun fetchAuthorQuotes(authorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { quoteApi.getAuthorQuotes(authorId) },
                onDataReady = {
                    val quoteEntities = it.quotes.map { it.toQuoteEntity() }
                    _author.value = it.author
                    _quotes.value = quoteEntities
                    storeQuotes(quoteEntities)
                }
            ).collect(_quoteResult)
        }
    }

    private fun storeQuotes(quotes: List<QuoteEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteDao.insertQuotes(quotes)
        }
    }
}