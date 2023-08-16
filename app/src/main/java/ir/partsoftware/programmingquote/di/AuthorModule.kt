package ir.partsoftware.programmingquote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.partsoftware.programmingquote.core.AppDatabase
import ir.partsoftware.programmingquote.database.author.AuthorDao
import ir.partsoftware.programmingquote.network.author.AuthorApi
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AuthorModule {

    @Provides
    fun provideAuthorApi(retrofit: Retrofit): AuthorApi {
        return retrofit.create(AuthorApi::class.java)
    }

    @Provides
    fun provideAuthorDao(appDatabase: AppDatabase): AuthorDao {
        return appDatabase.authorDao()
    }
}