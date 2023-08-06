package ir.partsoftware.programmingquote.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.network.author.Author
import ir.partsoftware.programmingquote.network.quote.Quote
import ir.partsoftware.programmingquote.network.search.SearchApi
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchApi: SearchApi
) : ViewModel() {

    private val _searchResult = MutableStateFlow<Result>(Result.Idle)
    val searchResult: StateFlow<Result> = _searchResult.asStateFlow()

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _authors = MutableStateFlow<List<Author>>(emptyList())
    val authors: StateFlow<List<Author>> = _authors.asStateFlow()

    private val _query = MutableStateFlow<String>("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null

    fun search() {
        searchJob?.cancel()

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            _searchResult.value = Result.Loading

            try {
                val response = searchApi.search(query.value)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _authors.value = body.authors
                        _quotes.value = body.quotes
                        _searchResult.value = Result.Success
                    } else {
                        _searchResult.value = Result.Error("whoops body was empty")
                    }
                } else {
                    _searchResult.value = Result.Error("whoops: got ${response.code()} code!")
                }
            } catch (t: Throwable) {
                _searchResult.value = Result.Error("whoops: ${t.message}")
            }
            searchJob = null
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }
}