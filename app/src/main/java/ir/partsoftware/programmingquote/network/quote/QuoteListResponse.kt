package ir.partsoftware.programmingquote.network.quote

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.network.author.AuthorResponse

@JsonClass(generateAdapter = true)
data class QuoteListResponse(
    val authorResponse: AuthorResponse,
    val quoteResponses: List<QuoteResponse>
)
