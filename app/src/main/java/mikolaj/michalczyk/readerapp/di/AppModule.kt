package mikolaj.michalczyk.readerapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mikolaj.michalczyk.readerapp.network.BooksAPI
import mikolaj.michalczyk.readerapp.repository.BookRepository
import mikolaj.michalczyk.readerapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideBookRepository(api:BooksAPI) = BookRepository(api)

    @Singleton
    @Provides
    fun provideBooksAPI(): BooksAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksAPI::class.java)
    }
}