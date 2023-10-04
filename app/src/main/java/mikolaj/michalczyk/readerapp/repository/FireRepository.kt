package mikolaj.michalczyk.readerapp.repository


import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import mikolaj.michalczyk.readerapp.data.DataOrException
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.model.MUser
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryBook: Query
) {
    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data =  queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }
// to-do fix getting users not from firestore but from authentication
    suspend fun getAllUsersFromDatabase(): DataOrException<List<MUser>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MUser>, Boolean, Exception>()
        try {
            dataOrException.loading = true
            dataOrException.data =  queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MUser::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }

}
