package ir.partsoftware.programmingquote.network.quote

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.database.quote.QuoteEntity

@JsonClass(generateAdapter = true)
data class Quote(
    val id: String,
    val text: String,
    val author: String
)

fun Quote.toQuoteEntity() = QuoteEntity(
    id = id,
    text = text,
    authorId = author
)