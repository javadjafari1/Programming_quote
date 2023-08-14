package ir.partsoftware.programmingquote.ui.screens.quote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.database.author.AuthorDao
import ir.partsoftware.programmingquote.database.quote.QuoteDao
import ir.partsoftware.programmingquote.database.quote.QuoteEntity
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
class QuoteViewModel @Inject constructor(
    private val authorDao: AuthorDao,
    private val quoteDao: QuoteDao,
    private val quoteApi: QuoteApi,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _quoteResult = MutableStateFlow<Result>(Result.Idle)
    val quoteResult: SharedFlow<Result> = _quoteResult.asSharedFlow()

    private val _quote = MutableStateFlow<QuoteEntity?>(null)
    val quote: StateFlow<QuoteEntity?> = _quote.asStateFlow()

    private val id: String get() = savedStateHandle.get<String>("id").orEmpty()

    init {
        getQuote(id)
        fetchQuote(id)
    }

    private fun getQuote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteDao.getQuote(id).collect(_quote)
        }
    }

    fun fetchQuote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { quoteApi.getQuoteById(id) },
                onDataReady = {
                    val quoteEntity = it.quote.toQuoteEntity()
                    _quote.value = quoteEntity
                    storeQuote(quoteEntity)
                }
            ).collect(_quoteResult)
        }
    }

    private fun storeQuote(quoteEntity: QuoteEntity) {
        viewModelScope.launch {
            quoteDao.insertQuote(quoteEntity)
        }
    }
}