package mikolaj.michalczyk.readerapp.screens.friends

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mikolaj.michalczyk.readerapp.data.DataOrException
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.model.MUser
import mikolaj.michalczyk.readerapp.repository.FireRepository
import javax.inject.Inject

@HiltViewModel
class FriendsScreenViewModel @Inject constructor(
    private val repository: FireRepository): ViewModel() {
    val userData: MutableState<DataOrException<List<MUser>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))
    val bookData: MutableState<DataOrException<List<MBook>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))

    init {
        getAllUsersFromDatabase()
        getAllBooksFromDatabase()
    }

    private fun getAllUsersFromDatabase() {
        viewModelScope.launch {
            userData.value.loading = true
            userData.value = repository.getAllUsersFromDatabase()
            if (!userData.value.data.isNullOrEmpty()) userData.value.loading = false
        }
        Log.d("GET", "getAllUsersFromDatabase: ${userData.value.data?.toList().toString()}")
    }
    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            bookData.value.loading = true
            bookData.value = repository.getAllBooksFromDatabase()
            if (!bookData.value.data.isNullOrEmpty()) userData.value.loading = false
        }
        Log.d("GET", "getAllBooksFromDatabase: ${userData.value.data?.toList().toString()}")
    }
}