package ir.partsoftware.programmingquote.network.search

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("v1/search")
    suspend fun search(
        @Query("query") query: String
    ): Response<SearchResponse>
}