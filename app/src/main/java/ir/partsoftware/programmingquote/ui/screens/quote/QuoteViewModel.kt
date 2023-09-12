package ir.partsoftware.programmingquote.ui.screens.quote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.database.dao.AuthorDao
import ir.partsoftware.programmingquote.database.dao.QuoteDao
import ir.partsoftware.programmingquote.database.entity.AuthorEntity
import ir.partsoftware.programmingquote.database.entity.QuoteEntity
import ir.partsoftware.programmingquote.network.common.safeApi
import ir.partsoftware.programmingquote.network.quote.QuoteApi
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
class QuoteViewModel @Inject constructor(
    private val quoteApi: QuoteApi,
    private val quoteDao: QuoteDao,
    private val authorDao: AuthorDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _quoteResult = MutableStateFlow<Result>(Result.Idle)
    val quoteResult: SharedFlow<Result> = _quoteResult.asSharedFlow()

    private val _quote = MutableStateFlow<QuoteEntity?>(null)
    val quote: StateFlow<QuoteEntity?> = _quote.asStateFlow()

    private val _author = MutableStateFlow<AuthorEntity?>(null)
    val author: StateFlow<AuthorEntity?> = _author.asStateFlow()

    private val id: String get() = savedStateHandle.get<String>("id").orEmpty()

    init {
        observeQuoteWithAuthor(id)
        fetchQuote(id)
    }

    private fun observeQuoteWithAuthor(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteDao.observeQuoteWithAuthor(id).collect {
                _author.emit(it.author)
                _quote.emit(it.quote)
            }
        }
    }

    fun fetchQuote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { quoteApi.getQuoteById(id) },
                onDataReady = { response ->
                    storeQuote(response.quote.toQuoteEntity())
                    storeAuthor(response.author.toAuthorEntity())
                }
            ).collect(_quoteResult)
        }
    }

    private fun storeAuthor(authorEntity: AuthorEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            authorDao.updateAuthor(authorEntity)
        }
    }

    private fun storeQuote(quoteEntity: QuoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteDao.updateQuote(quoteEntity)
        }
    }
}