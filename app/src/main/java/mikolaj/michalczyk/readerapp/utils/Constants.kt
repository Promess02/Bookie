package mikolaj.michalczyk.readerapp.utils

import androidx.compose.ui.graphics.Color

object Constants {
    //https://www.googleapis.com/books/v1/volumes?q=android
    const val BASE_URL = "https://www.googleapis.com/books/v1/"
    val APP_GRADIENT: List<Color> = listOf(Color(0xFFE68D65), Color(0xFFF0E5BD))
    val SIDE_GRADIENT: List<Color> = listOf(Color(0xFFC4A493),Color(0xFFF0E5BD))
    val APP_COLOR: Color = Color(0xFFA25138)

    val QUOTES_LIST: List<String> = listOf("There is no friend as loyal as a book",
    "A Book is a gift you can open again and again",
    "A writer only begins a book. A reader finishes it",
        "A Reader lives a thousand lives before he dies",
        "Books are a uniquely portable magic",
        "Books let you travel without moving your feet",
        "If you don't like to read, you haven't found the right book"
    )
}