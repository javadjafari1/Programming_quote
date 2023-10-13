package ir.partsoftware.programmingquote.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.network.author.AuthorResponse
import ir.partsoftware.programmingquote.network.common.safeApi
import ir.partsoftware.programmingquote.network.quote.QuoteAuthorResponse
import ir.partsoftware.programmingquote.network.search.SearchApi
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
class SearchViewModel @Inject constructor(
    private val searchApi: SearchApi
) : ViewModel() {

    private val _searchResult = MutableStateFlow<Result>(Result.Idle)
    val searchResult: SharedFlow<Result> = _searchResult.asSharedFlow()

    private val _quotes = MutableStateFlow<List<QuoteAuthorResponse>>(emptyList())
    val quotes: StateFlow<List<QuoteAuthorResponse>> = _quotes.asStateFlow()

    private val _authors = MutableStateFlow<List<AuthorResponse>>(emptyList())
    val authors: StateFlow<List<AuthorResponse>> = _authors.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            searchResult
                .filter { it is Result.Error }
                .collectLatest {
                    Log.e("${this@SearchViewModel::class.simpleName}", "$it")
                }
        }
    }

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