package ir.partsoftware.programmingquote.ui.screens.quotesist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.database.dao.AuthorDao
import ir.partsoftware.programmingquote.database.dao.QuoteDao
import ir.partsoftware.programmingquote.database.entity.AuthorEntity
import ir.partsoftware.programmingquote.database.entity.QuoteEntity
import ir.partsoftware.programmingquote.database.relation.AuthorWithQuotes
import ir.partsoftware.programmingquote.network.common.safeApi
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesListViewModel @Inject constructor(
    private val quoteApi: QuoteApi,
    private val savedStateHandle: SavedStateHandle,
    private val authorDao: AuthorDao,
    private val quoteDao: QuoteDao
) : ViewModel() {
    private val _quoteResult = MutableStateFlow<Result>(Result.Idle)
    val quoteResult: SharedFlow<Result> = _quoteResult.asSharedFlow()

    private val _quotes = MutableStateFlow<List<QuoteEntity>>(emptyList())
    val quotes: StateFlow<List<QuoteEntity>> = _quotes.asStateFlow()

    private val _author = MutableStateFlow<AuthorEntity?>(null)
    val author: StateFlow<AuthorEntity?> = _author.asStateFlow()

    private val authorId: String
        get() = savedStateHandle.get<String>("authorId").orEmpty()

    init {
        observeAuthorWithQuotes(id = authorId)
        fetchAuthorQuote(authorId)
    }

    private fun observeAuthorWithQuotes(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authorDao.observeAuthorWithQuotes(id).collect {
                _author.emit(it.author)
                _quotes.emit(it.quotes)
            }
        }
    }

    fun fetchAuthorQuote(authorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { quoteApi.getAuthorQuotes(authorId) },
                onDataReady = { response ->
                    val author = response.author.toAuthorEntity()
                    val quotes = response.quotes.map {
                        quote->
                        quote.toQuoteEntity() }
                    storeAuthor(author)
                    storeQuotes(quotes)
                }
            ).collect(_quoteResult)
        }
    }

    private fun storeAuthor(authorEntity: AuthorEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            authorDao.updateAuthor(authorEntity)
        }
    }

    private fun storeQuotes(quotes: List<QuoteEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteDao.insertQuotes(quotes)
        }
    }
}