package mikolaj.michalczyk.readerapp.repository

import mikolaj.michalczyk.readerapp.data.DataOrException
import mikolaj.michalczyk.readerapp.data.Resource
import mikolaj.michalczyk.readerapp.model.Item
import mikolaj.michalczyk.readerapp.network.BooksAPI
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksAPI) {
    private val dataOrException = DataOrException<List<Item>,Boolean,Exception>()
    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()
    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
       return try {
           Resource.Loading(data = "Loading...")
           val itemList = api.getAllBooks(searchQuery).items
           if(itemList.isNotEmpty()) Resource.Loading(data = false)
           Resource.Success(data = itemList)
       }catch (exception: Exception){
           Resource.Error(message = exception.message.toString())
       }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item>{
        val response = try{
            Resource.Loading(data = true)
            api.getBookInfo(bookId)
        }catch(e: Exception){
            return Resource.Error(message = "An error occurred ${e.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

}