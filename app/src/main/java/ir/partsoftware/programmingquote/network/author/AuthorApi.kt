package ir.partsoftware.programmingquote.network.author

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthorApi {

    @GET("v1/author")
    suspend fun getAuthors(
        @Query("count") count: Int? = null // 100
    ): Response<List<AuthorResponse>>

}