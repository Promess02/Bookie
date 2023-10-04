package mikolaj.michalczyk.readerapp.screens.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import mikolaj.michalczyk.readerapp.R
import mikolaj.michalczyk.readerapp.components.ReaderAppBarAlt
import mikolaj.michalczyk.readerapp.components.nullText
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.screens.home.HomeScreenViewModel
import mikolaj.michalczyk.readerapp.utils.Constants.APP_COLOR
import mikolaj.michalczyk.readerapp.utils.formatDate
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatsUpdateScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel())
{
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser
    
    Scaffold(topBar = {
        ReaderAppBarAlt(title = "Stats Screen",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false){
            navController.popBackStack()
        }
    }) {
        Surface {
            books = if(!viewModel.data.value.data.isNullOrEmpty()){
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            }else{
                emptyList()
            }
            Column {
                Row(modifier = Modifier.padding(start = 10.dp, top = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .size(140.dp)
                        .padding(10.dp),
                        contentAlignment = Alignment.Center) {
                        Surface(modifier = Modifier
                            .padding(10.dp)
                            .size(140.dp),
                            color = Color.White,
                            shape = CircleShape,
                            border = BorderStroke(width = 2.dp,color = APP_COLOR)) {
                            Image(
                                painter = rememberImagePainter(data = R.drawable.ic_baseline_menu_book_24),
                                contentDescription = "book image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(end = 4.dp),
                            )
                        }
                    }
                    Text(text = "Hi, ${currentUser?.email.toString().split("@")[0].uppercase(Locale.ROOT)}",
                        color = APP_COLOR,
                        style = MaterialTheme.typography.h5)
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp
                ) {
                    val readBooksList: List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()){
                        books.filter{ mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading !=null)
                        }
                    }else{
                        emptyList()
                    }

                    val readingBooks = books.filter{mbook ->
                        (mbook.startedReading != null && mbook.finishedReading ==null)
                    }

                    Column(modifier = Modifier.padding(start = 25.dp,top = 8.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.Start){
                        Text(text = "Your Stats", style = MaterialTheme.typography.h5, color = APP_COLOR)
                        Divider()
                        Text(text = "You are reading: ${readingBooks.size} books")
                        Text(text = "You have read: ${readBooksList.size} books")

                    }
                }
                if(viewModel.data.value.loading == true){
                LinearProgressIndicator()
            }else{
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    contentPadding = PaddingValues(16.dp)){
                    val readBooks: List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()){
                        viewModel.data.value.data!!.filter{mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading !=null)
                        }
                    }else{
                        emptyList()
                    }
                    items(items = readBooks){ mBook ->
                        BookRowStats(book = mBook)

                    }
                }
            }
            }


            }

        
    }
}

@Composable
fun BookRowStats(
    book: MBook) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 7.dp) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top) {

            val dateFormat = SimpleDateFormat("dd.MM.yyyy")

            val imageUrl = if(book.photoUrl.toString().isEmpty()) "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
            else book.photoUrl.toString()
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp),
            )

            Column {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.h6,
                color = APP_COLOR)
                if (!book.authors.isNullOrEmpty()){
                    Text(text =  "Author: ${book.authors.toString()}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()

                if(book.startedReading!=null){
                    Text(text =  "Started: ${dateFormat.format(book.startedReading!!.toDate())}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()

                if(book.finishedReading!=null){
                    Text(text =  "Finished: ${dateFormat.format(book.finishedReading!!.toDate())}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()
            }
        }
    }

}

@Preview
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatsUpdateScreenPreview()
{
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold{
        Surface {
            books = listOf(MBook(
                    title = "nowa",
            authors = "afsfsasaf",
            notes = "",
            photoUrl = "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80",
            categories = "gaassafafs",
            publishedData = "affasfasf",
            rating = 0.0,
            isFavourite = false,
            description = "aasffsaafssaffa",
            pageCount = "1232",
            userId = "gaffsasaffas",
            googleBookId = "faafsSAF",
                startedReading = Timestamp.now(),
                finishedReading = Timestamp.now()
            ))

            Column {
                Row(modifier = Modifier.padding(start = 10.dp, top = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .size(140.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center) {
                        Surface(modifier = Modifier
                            .padding(10.dp)
                            .size(140.dp),
                            color = Color.White,
                            shape = CircleShape,
                            border = BorderStroke(width = 2.dp,color = APP_COLOR)) {
                            Image(
                                painter = rememberImagePainter(data = R.drawable.ic_baseline_menu_book_24),
                                contentDescription = "book image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(end = 4.dp),
                            )
                        }
                    }
                    Text(text = "Hi, ${currentUser?.email.toString().split("@")[0].uppercase(Locale.ROOT)}",
                        color = APP_COLOR,
                        style = MaterialTheme.typography.h5)
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp
                ) {
                    val readBooksList: List<MBook> = books.filter { mBook ->
                        (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                    }

                    val readingBooks = books.filter{mbook ->
                        (mbook.startedReading != null && mbook.finishedReading ==null)
                    }

                    Column(modifier = Modifier.padding(start = 25.dp,top = 8.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text(text = "Your Stats", style = MaterialTheme.typography.h5, color = APP_COLOR)
                        Divider()
                        Text(text = "You are reading: ${readingBooks.size} books")
                        Text(text = "You have read: ${readBooksList.size} books")

                    }

                }
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        contentPadding = PaddingValues(16.dp)){
                        val readBooks: List<MBook> =
                            books.filter{mBook ->
                                (mBook.finishedReading !=null)
                            }

                        items(items = readBooks){ mBook ->
                            BookRowStats(book = mBook)
                        }
                    }

            }


        }


    }
}