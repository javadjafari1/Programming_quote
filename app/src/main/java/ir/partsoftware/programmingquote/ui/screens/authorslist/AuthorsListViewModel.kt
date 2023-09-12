package ir.partsoftware.programmingquote.ui.screens.authorslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.partsoftware.programmingquote.database.dao.AuthorDao
import ir.partsoftware.programmingquote.database.entity.AuthorEntity
import ir.partsoftware.programmingquote.network.author.AuthorApi
import ir.partsoftware.programmingquote.network.common.safeApi
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import ir.partsoftware.programmingquote.network.quote.QuoteWithAuthorResponse
import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorsListViewModel @Inject constructor(
    private val authorApi: AuthorApi,
    private val quoteApi: QuoteApi,
    private val authorDao: AuthorDao
) : ViewModel() {

    private val _authorResult = MutableStateFlow<Result>(Result.Idle)
    val authorResult: SharedFlow<Result> = _authorResult.asSharedFlow()

    private val _randomResult = MutableSharedFlow<Result>()
    val randomResult: SharedFlow<Result> = _randomResult.asSharedFlow()

    private val _authors = MutableStateFlow<List<AuthorEntity>>(emptyList())
    val authors: StateFlow<List<AuthorEntity>> = _authors.asStateFlow()

    private val _randomQuote = MutableStateFlow<QuoteWithAuthorResponse?>(null)
    val randomQuote: StateFlow<QuoteWithAuthorResponse?> = _randomQuote.asStateFlow()

    init {
        observeAuthors()
        fetchAuthors()
    }

    private fun observeAuthors(){
        viewModelScope.launch(Dispatchers.IO) {
            authorDao.observeAuthors().collect(_authors)
        }
    }

    fun fetchAuthors() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { authorApi.getAuthors() },
                onDataReady = { authorsResponse->
                    storeAuthors(authorsResponse.map { it.toAuthorEntity() })
                }
            ).collect(_authorResult)
        }
    }

    private fun storeAuthors(authors:List<AuthorEntity>){
        viewModelScope.launch(Dispatchers.IO) {
            authorDao.insertAuthors(authors)
        }
    }

    fun getRandomQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApi(
                call = { quoteApi.getRandomQuote() },
                onDataReady = { _randomQuote.value = it }
            ).collect(_randomResult)
        }
    }
}