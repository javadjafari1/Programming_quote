package ir.partsoftware.programmingquote.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.network.author.Author
import ir.partsoftware.programmingquote.network.common.safeApi
import ir.partsoftware.programmingquote.network.quote.Quote
import ir.partsoftware.programmingquote.network.quote.QuoteResponse
import ir.partsoftware.programmingquote.network.search.SearchApi
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchApi: SearchApi
) : ViewModel() {

    private val _searchResult = MutableStateFlow<Result>(Result.Idle)
    val searchResult: SharedFlow<Result> = _searchResult.asSharedFlow()

    private val _quotes = MutableStateFlow<List<QuoteResponse>>(emptyList())
    val quotes: StateFlow<List<QuoteResponse>> = _quotes.asStateFlow()

    private val _authors = MutableStateFlow<List<Author>>(emptyList())
    val authors: StateFlow<List<Author>> = _authors.asStateFlow()

    private val _query = MutableStateFlow<String>("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null

    fun search() {
        searchJob?.cancel()

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { searchApi.search(query.value) },
                onDataReady = {
                    _authors.value = it.authors
                    _quotes.value = it.quotes
                }
            ).collect(_searchResult)
            searchJob = null
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }
}