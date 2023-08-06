package ir.partsoftware.programmingquote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object QuoteModule {

    @Provides
    fun provideQuoteApi(retrofit: Retrofit): QuoteApi {
        return retrofit.create(QuoteApi::class.java)
    }
}