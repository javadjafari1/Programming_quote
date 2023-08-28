package ir.partsoftware.programmingquote.network.author

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.database.author.AuthorEntity

@JsonClass(generateAdapter = true)
data class AuthorResponse(
    val id: String,
    val name: String,
    val extract: String?,
    val infoUrl: String?,
    val image: String?,
    val quoteCount: Int?
) {
    fun toAuthorEntity() = AuthorEntity(
        id = id,
        name = name,
        extract = extract,
        infoUrl = infoUrl,
        image = image,
        quoteCount = quoteCount
    )
}

