package ir.partsoftware.programmingquote.ui.screens.quote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.database.author.AuthorDao
import ir.partsoftware.programmingquote.database.author.AuthorEntity
import ir.partsoftware.programmingquote.database.quote.QuoteDao
import ir.partsoftware.programmingquote.database.quote.QuoteEntity
import ir.partsoftware.programmingquote.database.relation.QuoteWithAuthor
import ir.partsoftware.programmingquote.network.common.safeApi
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteDao: QuoteDao,
    private val authorDao: AuthorDao,
    private val quoteApi: QuoteApi,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _quoteResult = MutableStateFlow<Result>(Result.Idle)
    val quoteResult: SharedFlow<Result> = _quoteResult.asSharedFlow()

    private val _quoteWithAuthor = MutableStateFlow<QuoteWithAuthor?>(null)
    val quoteWithAuthor: StateFlow<QuoteWithAuthor?> = _quoteWithAuthor.asStateFlow()

    private val id: String get() = savedStateHandle.get<String>("id").orEmpty()

    init {
        observeQuote(id)
        fetchQuote(id)

        viewModelScope.launch {
            quoteResult
                .filter { it is Result.Error }
                .collectLatest {
                    Log.e("${this@QuoteViewModel::class.simpleName}", "$it")
                }
        }
    }

    private fun observeQuote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteDao.observeQuoteWithAuthor(id).collect(_quoteWithAuthor)
        }
    }

    fun fetchQuote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { quoteApi.getQuoteById(id) },
                onDataReady = {
                    val quoteEntity = it.quote.toQuoteEntity()
                    val authorEntity = it.author.toAuthorEntity()
                    _quoteWithAuthor.value = QuoteWithAuthor(
                        quote = quoteEntity,
                        author = authorEntity
                    )
                    storeQuote(quoteEntity)
                    storeAuthor(authorEntity)
                }
            ).collect(_quoteResult)
        }
    }

    private fun storeQuote(quoteEntity: QuoteEntity) {
        viewModelScope.launch {
            quoteDao.insertQuote(quoteEntity)
        }
    }

    private fun storeAuthor(author: AuthorEntity) {
        viewModelScope.launch {
            authorDao.insertAuthor(author)
        }
    }
}