package mikolaj.michalczyk.readerapp.screens.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mikolaj.michalczyk.readerapp.components.*
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.navigation.ReaderScreens
import mikolaj.michalczyk.readerapp.utils.Constants.APP_GRADIENT

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val contextForToast = LocalContext.current.applicationContext
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
        ReaderAppBar(title = "Bookie", navController = navController) {
            coroutineScope.launch {
            scaffoldState.drawerState.open()
          }
        }
    },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        },
    drawerContent = {
        DrawerContent(navController = navController){ itemLabel ->
            Toast
                .makeText(contextForToast, itemLabel, Toast.LENGTH_SHORT)
                .show()
            coroutineScope.launch {
                // delay for the ripple effect
                delay(timeMillis = 250)
                scaffoldState.drawerState.close()
            }
        }
    }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController, viewModel)
        }
    }
}
@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel){

    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }

        Log.d("Books", "HomeContent: ${listOfBooks.toString()}")
    }

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")
            ?.get(0)else "N/A"
    Column(Modifier.background(brush = Brush.verticalGradient(APP_GRADIENT)),
    verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your reading \n " + "activity right now:")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
        }
        ReadingRightNowArea(listOfBooks = listOfBooks,
            navController =navController )
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks = listOfBooks,
            navController = navController)
    }


}

@Composable
fun BookListArea(listOfBooks: List<MBook>,
                 navController: NavController) {
    val addedBooks = listOfBooks.filter { mBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(addedBooks){
        navController.navigate(ReaderScreens.UpdateScreen.name +"/$it")
    }



}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>,
                                  viewModel: HomeScreenViewModel = hiltViewModel(),
                                  onCardPressed: (String) -> Unit) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)) {
        if (viewModel.data.value.loading == true) {
            LinearProgressIndicator()

        }else {
            if (listOfBooks.isNullOrEmpty()){
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No books found. Add a Book",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )

                }
            }else {
                for (book in listOfBooks) {
                    ListCard(book) {
                        onCardPressed(book.googleBookId.toString())

                    }
                }
            }

        }

    }


}


@Composable
fun ReadingRightNowArea(listOfBooks: List<MBook>,
                        navController: NavController) {
    //Filter books by reading now
    val readingNowList = listOfBooks.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(readingNowList){
        Log.d("TAG", "BookListArea: $it")
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }



}

