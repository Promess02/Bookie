package mikolaj.michalczyk.readerapp.screens.favourite

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import mikolaj.michalczyk.readerapp.components.ReaderAppBarAlt
import mikolaj.michalczyk.readerapp.components.nullText
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.screens.home.HomeScreenViewModel
import mikolaj.michalczyk.readerapp.screens.stats.BookRowStats
import mikolaj.michalczyk.readerapp.utils.formatDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FavouriteScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(topBar = {
        ReaderAppBarAlt(title = "Book Stats", icon = Icons.Default.ArrowBack,
            navController = navController){
            navController.popBackStack()
        }
    }){

        Surface{
            books = if(!viewModel.data.value.data.isNullOrEmpty()){
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            }else{
                emptyList()
            }
            val favBooksList = books.filter { book ->
                book.isFavourite == true
            }

            Column{
                Column(modifier = Modifier.padding(start = 25.dp,top = 4.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.Start) {
                    Text(text = "You have ${favBooksList.size}  favourite books")
                }
                if(viewModel.data.value.loading == true){
                    LinearProgressIndicator()
                }else{
                    Divider()
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        contentPadding = PaddingValues(16.dp)
                    ){
                        items(items = favBooksList){ mBook ->
                            BookRowFav(book = mBook)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookRowFav(
    book: MBook) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top) {

            val imageUrl = if(book.photoUrl.toString().isEmpty()) "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
            else book.photoUrl.toString()
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp),
            )

            Column {
                if(!book.title.isNullOrEmpty()) {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
            } else nullText()

                if (!book.authors.isNullOrEmpty()){
                    Text(text =  "Author: ${book.authors.toString().replace(Regex("[\\[\\]]"), "")}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()


                if(!book.publishedData.isNullOrEmpty()) {
                    Text(text =  "Date: ${book.publishedData.toString().replace(Regex("[\\[\\]]"), "")}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()


                if(!book.categories.isNullOrEmpty()){
                    Text(text = book.categories.toString().replace(Regex("[\\[\\]]"), ""),
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()

            }

        }

    }

}