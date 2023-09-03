package ir.partsoftware.programmingquote.network.search

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.network.author.AuthorResponse
import ir.partsoftware.programmingquote.network.quote.QuoteAuthorResponse

@JsonClass(generateAdapter = true)
data class SearchResponse(
    val authorResponses: List<AuthorResponse>,
    val quotes: List<QuoteAuthorResponse>
)
