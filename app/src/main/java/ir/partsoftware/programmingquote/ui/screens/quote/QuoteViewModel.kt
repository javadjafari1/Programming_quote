package ir.partsoftware.programmingquote.ui.screens.quote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import ir.partsoftware.programmingquote.network.quote.QuoteResponse
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteApi: QuoteApi,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _quoteResult = MutableStateFlow<Result>(Result.Idle)
    val quoteResult: StateFlow<Result> = _quoteResult.asStateFlow()

    private val _quote = MutableStateFlow<QuoteResponse?>(null)
    val quote: StateFlow<QuoteResponse?> = _quote.asStateFlow()

    private val id: String get() = savedStateHandle.get<String>("id").orEmpty()

    init {
        getQuote(id)
    }

    fun getQuote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _quoteResult.value = Result.Loading

            try {
                val response = quoteApi.getQuoteById(id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _quote.value = body
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