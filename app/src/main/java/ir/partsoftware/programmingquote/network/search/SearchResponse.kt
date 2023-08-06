package ir.partsoftware.programmingquote.network.search

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.network.author.Author
import ir.partsoftware.programmingquote.network.quote.Quote

@JsonClass(generateAdapter = true)
data class SearchResponse(
    val authors: List<Author>,
    val quotes: List<Quote>
)
