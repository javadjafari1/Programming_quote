package ir.partsoftware.programmingquote.network.quote

import retrofit2.Response
import retrofit2.http.GET

interface QuoteApi {

    @GET("v1/quote/random")
    suspend fun getRandomQuote(): Response<RandomQuoteResponse>
}