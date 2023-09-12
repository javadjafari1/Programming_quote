package ir.partsoftware.programmingquote.network.quote

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.network.author.Author

@JsonClass(generateAdapter = true)
data class QuoteWithAuthorResponse(
    val author: Author,
    val quote: Quote
)