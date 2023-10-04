package mikolaj.michalczyk.readerapp.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mikolaj.michalczyk.readerapp.data.Resource
import mikolaj.michalczyk.readerapp.model.Item
import mikolaj.michalczyk.readerapp.repository.BookRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val repository: BookRepository): ViewModel() {
    suspend fun getBookInfo(bookId: String): Resource<Item>{

        return repository.getBookInfo(bookId)
    }
}