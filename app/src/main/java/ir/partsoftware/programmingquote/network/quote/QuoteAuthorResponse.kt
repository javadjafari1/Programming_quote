package ir.partsoftware.programmingquote.network.quote

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.network.author.AuthorResponse

@JsonClass(generateAdapter = true)
data class QuoteAuthorResponse(
    val author: AuthorResponse,
    val quote: QuoteResponse
)