package mikolaj.michalczyk.readerapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import mikolaj.michalczyk.readerapp.components.*
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.navigation.ReaderScreens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        ReaderAppBar(title = "A.Reader", navController = navController)
    },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController)
        }
    }
}
@Composable
fun HomeContent(navController: NavController){
    val listOfBooks = listOf<MBook>(
        MBook(id = "dadsaa", title = " affafa", authors = "afsfa", notes = "afa sffa"),
        MBook(id = "dadsaa", title = " affafa", authors = "afsfa", notes = "afa sffa"),
        MBook(id = "dadsaa", title = " affafa", authors = "afsfa", notes = "afa sffa"),
        MBook(id = "dadsaa", title = " affafa", authors = "afsfa", notes = "afa sffa"),
        MBook(id = "dadsaa", title = " affafa", authors = "afsfa", notes = "afa sffa")
    )
    val currentUserName = if(!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)else "N/A"
    Column(Modifier.padding(2.dp),
    verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your reading \n " + "activity right now:")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Profile",
                modifier = Modifier
                    .clickable {
                        navController.navigate(ReaderScreens.StatsScreen.name)
                    }
                    .size(45.dp),
                tint = MaterialTheme.colors.secondaryVariant)
                Text(text = currentUserName!!,
                modifier = Modifier.padding(2.dp),
                style = MaterialTheme.typography.overline,
                color = Color.Red,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Clip)
                Divider()

            }
        }
       ReadingRightNowArea(books = listOfBooks,
            navController = navController)
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }


}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    HorizontalScrollableComponent(listOfBooks){
        //Todo: on card clicked navigate to details
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPressed: (String) -> Unit = {}) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)) {
        for( book in listOfBooks){
            ListCard(book){
                onCardPressed(it)
            }
        }
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController){
    ListCard(books.get(0))
}

