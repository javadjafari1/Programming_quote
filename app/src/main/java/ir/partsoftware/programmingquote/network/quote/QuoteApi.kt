package ir.partsoftware.programmingquote.network.quote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuoteApi {

    @GET("v1/quote/random")
    suspend fun getRandomQuote(): Response<QuoteAuthorResponse>

    @GET("v1/quote")
    suspend fun getAuthorQuotes(
        @Query("author") authorId: String
    ): Response<QuoteListResponse>

    @GET("v1/quote/{id}")
    suspend fun getQuoteById(
        @Path("id") quoteId: String
    ): Response<QuoteAuthorResponse>
}