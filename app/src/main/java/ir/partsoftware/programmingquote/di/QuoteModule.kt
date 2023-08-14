package ir.partsoftware.programmingquote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.partsoftware.programmingquote.core.AppDatabase
import ir.partsoftware.programmingquote.database.author.AuthorDao
import ir.partsoftware.programmingquote.database.quote.QuoteDao
import ir.partsoftware.programmingquote.network.quote.QuoteApi
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object QuoteModule {

    @Provides
    fun provideQuoteApi(retrofit: Retrofit): QuoteApi {
        return retrofit.create(QuoteApi::class.java)
    }

    @Provides
    fun provideAuthorDao(appDatabase: AppDatabase): AuthorDao {
        return appDatabase.authorDao()
    }

    @Provides
    fun provideQuoteDao(appDatabase: AppDatabase): QuoteDao {
        return appDatabase.quoteDao()
    }
}