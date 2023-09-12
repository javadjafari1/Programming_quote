package ir.partsoftware.programmingquote.network.quote

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.database.entity.QuoteEntity

@JsonClass(generateAdapter = true)
data class Quote(
    val id: String,
    val text: String,
    val author: String
) {
    fun toQuoteEntity() = QuoteEntity(id = this.id, text = this.text, authorId = this.author)
}
