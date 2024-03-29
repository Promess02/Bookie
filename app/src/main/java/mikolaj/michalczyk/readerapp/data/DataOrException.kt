package mikolaj.michalczyk.readerapp.data

data class DataOrException<T,Boolean, E:Exception>(
    var data: T?=null,
    var loading: Boolean? = null,
    var e: Exception? = null
)
