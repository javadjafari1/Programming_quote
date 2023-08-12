package ir.partsoftware.programmingquote.network.author

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Author(
    val id: String,
    val name: String,
    val extract: String?,
    val infoUrl: String?,
    val image: String?,
    val quoteCount: Int?
)