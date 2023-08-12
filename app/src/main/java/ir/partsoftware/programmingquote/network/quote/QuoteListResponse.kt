package ir.partsoftware.programmingquote.network.quote

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.network.author.Author

@JsonClass(generateAdapter = true)
data class QuoteListResponse(
    val author: Author,
    val quotes: List<Quote>
)
