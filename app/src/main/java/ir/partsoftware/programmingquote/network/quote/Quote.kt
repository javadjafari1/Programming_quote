package ir.partsoftware.programmingquote.network.quote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Quote(
    val id: String,
    val text: String,
    val author: String
)
