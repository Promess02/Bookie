package mikolaj.michalczyk.readerapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import mikolaj.michalczyk.readerapp.data.DataOrException
import mikolaj.michalczyk.readerapp.model.MBook
import javax.inject.Inject

class FireRepository @Inject constructor(private val queryBook: Query){
    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception>{
        val dataOrException = DataOrException<List<MBook>,Boolean, Exception>()

        try{
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map{ documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!

            }
            // pobiera przez api firebase i zapytanie z argumentu - queryBook.get()
            // await() - sprowadza na poboczny wątek i nie blokuje głównego
            // documents.map{} - pobiera dokument z bazy danych w firebase i mapuje do obiektu klasy MBook
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        }catch (exception:FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException
    }
}