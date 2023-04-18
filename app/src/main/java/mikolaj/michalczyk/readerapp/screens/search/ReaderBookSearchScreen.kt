package mikolaj.michalczyk.readerapp.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import mikolaj.michalczyk.readerapp.components.InputField
import mikolaj.michalczyk.readerapp.components.ReaderAppBarAlt
import mikolaj.michalczyk.readerapp.components.nullText
import mikolaj.michalczyk.readerapp.model.Item
import mikolaj.michalczyk.readerapp.navigation.ReaderScreens
import mikolaj.michalczyk.readerapp.utils.Constants.APP_GRADIENT


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun SearchScreen(navController: NavController,
                 viewModel: BooksSearchViewModel = hiltViewModel()
) {

    Scaffold(topBar = {
        ReaderAppBarAlt(title = "Search Books",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false){
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }
    }) {
        Surface {
            Column(modifier = Modifier.background(brush = Brush.verticalGradient(APP_GRADIENT))) {
                SearchForm { searchQuery ->
                    viewModel.searchBooks(query = searchQuery)

                }
                Spacer(modifier = Modifier.height(13.dp))
                BookList(navController = navController)

            }


        }
    }

}

@Composable
fun BookList(navController: NavController,
             viewModel: BooksSearchViewModel = hiltViewModel()) {


    val listOfBooks = viewModel.list
    if (viewModel.isLoading){
        Row(
            modifier = Modifier.padding(end = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator()
            Text(text = "Loading...")
        }

    }else {
        LazyColumn(modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)){
            items(items = listOfBooks) { book ->
                BookRow(book, navController)

            }

        }
    }

}

@Composable
fun BookRow(
    book: Item,
    navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RoundedCornerShape(CornerSize(3.dp)),
        elevation = 7.dp) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top) {

            val imageUrl = if(book.volumeInfo.imageLinks != null) book.volumeInfo.imageLinks.smallThumbnail
            else "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp),
            )

            Column {
                val bookInfo = book.volumeInfo
                if(!bookInfo.title.isNullOrEmpty()) {
                    Text(text = bookInfo.title, overflow = TextOverflow.Ellipsis)
                } else nullText()

                if(!bookInfo.authors.isNullOrEmpty()){
                    Text(text =  "Author: ${bookInfo.authors.toString().replace(Regex("[\\[\\]]"), "")}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()


                if(!bookInfo.publishedDate.isNullOrEmpty()) {
                    Text(text =  "Date: ${bookInfo.publishedDate.replace(Regex("[\\[\\]]"), "")}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()


                if(!bookInfo.categories.isNullOrEmpty()){
                    Text(text = bookInfo.categories.toString().replace(Regex("[\\[\\]]"), ""),
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption)
                } else nullText()
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun SearchForm(
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }

        InputField(valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })

    }
}
