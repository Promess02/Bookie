package mikolaj.michalczyk.readerapp.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mikolaj.michalczyk.readerapp.components.ReaderAppBarAlt
import mikolaj.michalczyk.readerapp.components.RoundedButton
import mikolaj.michalczyk.readerapp.data.Resource
import mikolaj.michalczyk.readerapp.model.Item
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.utils.Constants.SIDE_GRADIENT


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookDetailsScreen(navController: NavController,
                      bookId: String, viewModel: DetailsViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        ReaderAppBarAlt(
            title = "Book Details", navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false
        ){
            navController.popBackStack()
        }
    }) {
        Surface(modifier = Modifier
            .fillMaxSize()) {
            Column(modifier = Modifier
                .background(brush = Brush.verticalGradient(SIDE_GRADIENT))) {
                Column(modifier = Modifier
                    .padding(horizontal = 25.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
                        value = viewModel.getBookInfo(bookId)
                    }.value

                    ShowBookDetails(bookInfo = bookInfo, navController = navController)
                }

            }

        }

    }
}


@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavController){

    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id
    val cleanDescription = if(!bookData?.description.isNullOrEmpty()){
        bookInfo.data?.volumeInfo?.let { HtmlCompat.fromHtml(it.description,HtmlCompat.FROM_HTML_MODE_LEGACY).toString() }
    }else "no description provided"

    if(bookData == null) {
        Row {
            LinearProgressIndicator()
            Text(text = "Loading...")
        }
    }
    else{
            val imageUrl = if(bookInfo.data.volumeInfo.imageLinks != null) bookInfo.data.volumeInfo.imageLinks.thumbnail
            else "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .height(210.dp)
                    .width(140.dp),
            )
            Text(text = bookInfo.data.volumeInfo.title,
                style = MaterialTheme.typography.h5, fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )
            if (bookInfo.data.volumeInfo.authors.isNullOrEmpty()) Text(text = "Authors: no authors found",
                style = MaterialTheme.typography.body2, fontWeight = FontWeight.SemiBold)
            else{
                val authors =  bookInfo.data.volumeInfo.authors.toString()
                Text(text = "Authors: ${authors.substring(startIndex = 1, endIndex = authors.length-1)}",
                    style = MaterialTheme.typography.body2, fontWeight = FontWeight.SemiBold)
            }
            if(bookInfo.data.volumeInfo.categories.isNullOrEmpty()) Text(text = "Categories: no categories found",
                style = MaterialTheme.typography.body2, fontWeight = FontWeight.SemiBold)
            else{
                val formattedCategories = bookInfo.data.volumeInfo.categories.toString().replace(Regex("[\\[\\]]"), "")
                Text(text = "Categories: $formattedCategories",
                    style = MaterialTheme.typography.body2, fontWeight = FontWeight.SemiBold)
            }
        if(bookInfo.data.volumeInfo.publishedDate.isNullOrEmpty()) Text(text = "Published: No date found",
            style = MaterialTheme.typography.caption)
            else Text(text = "Published: ${bookInfo.data.volumeInfo.publishedDate}",
                style = MaterialTheme.typography.caption)

        if(bookInfo.data.volumeInfo.description.isNullOrEmpty()){}
        else{
            val localDims = LocalContext.current.resources.displayMetrics
            Surface(modifier = Modifier
                .height(localDims.heightPixels.dp.times(0.09f))
                .padding(4.dp), shape = RoundedCornerShape(8.dp), border = BorderStroke(width = 1.dp, Color.Blue)) {
                LazyColumn(modifier = Modifier
                    .padding(3.dp)
                    .height(200.dp)){
                    item{
                        if (cleanDescription != null) {
                            Text(text = cleanDescription)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                RoundedButton(label = "Save", radius = 30){
                    val book = MBook(
                        title = bookData.title,
                        authors = bookData.authors.toString(),
                        notes = "",
                        photoUrl = bookData.imageLinks?.thumbnail,
                        categories = bookData.categories.toString().replace(Regex("[\\[\\]]"), ""),
                        publishedData = bookData.publishedDate,
                        rating = 0.0,
                        isFavourite = false,
                        description = cleanDescription,
                        pageCount = bookData.pageCount.toString(),
                        userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        googleBookId = googleBookId
                    )
                    saveToFirebase(book, navController)
                }
                Spacer(modifier = Modifier.width(50.dp))
                RoundedButton(label = "Cancel", radius = 30){
                    navController.popBackStack()
                }
            }
        }
}

fun saveToFirebase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")
    if(book.toString().isNotEmpty()){
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
            }
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    navController.popBackStack()
                }
            }
            .addOnFailureListener {
                Log.w("Error", "saveToFirebase: Error updating doc", it)
            }
    }
}
