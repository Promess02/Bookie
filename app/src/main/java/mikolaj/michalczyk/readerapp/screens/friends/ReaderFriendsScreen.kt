package mikolaj.michalczyk.readerapp.screens.friends

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import mikolaj.michalczyk.readerapp.R
import mikolaj.michalczyk.readerapp.components.ReaderAppBarAlt
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.model.MUser
import mikolaj.michalczyk.readerapp.utils.Constants
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderFriendsScreen(navController: NavController, viewModel: FriendsScreenViewModel = hiltViewModel()) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val listOfUsers = viewModel.userData.value.data
    val listOfBooks = viewModel.bookData.value.data
    Scaffold(topBar = {
        ReaderAppBarAlt(title = "Friends Screen",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false){
            navController.popBackStack()
        }
    }) {
        Surface{
            //save, because there is always at least one user
                val user = listOfUsers?.toList()!!.filter{ u -> u.userId == currentUser?.uid }[0]
                val friends = arrayListOf<MUser>()
                for(userId in user.friendsList) {
                    friends.add(listOfUsers.toList().filter { u -> u.userId == userId }[0])
                }
                Column{
                 Row(modifier = Modifier.padding(start = 10.dp, top = 3.dp), verticalAlignment = Alignment.CenterVertically){
                     Box(modifier = Modifier
                         .size(140.dp)
                         .padding(10.dp),
                         contentAlignment = Alignment.Center) {
                         Surface(modifier = Modifier
                             .padding(10.dp)
                             .size(140.dp),
                             color = Color.White,
                             shape = CircleShape,
                             border = BorderStroke(width = 2.dp,color = Constants.APP_COLOR)
                         ) {
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
                         color = Constants.APP_COLOR,
                         style = MaterialTheme.typography.h5)
                 }
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                        shape = RoundedCornerShape(15.dp),
                        elevation = 5.dp
                    ) {
                        Column(modifier = Modifier.padding(start = 25.dp,top = 8.dp, bottom = 8.dp),
                            horizontalAlignment = Alignment.Start){
                            Text(text = "Your Stats", style = MaterialTheme.typography.h5, color = Constants.APP_COLOR)
                            Divider()
                            Text(text = "You have: ${friends.size} friends")
                        }
                    }
                    if(viewModel.userData.value.loading== true || viewModel.bookData.value.loading==true){
                        LinearProgressIndicator()
                }else{
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(16.dp)){
                            items(items = friends){ friend ->
                                if (listOfBooks != null) {
                                    UserStatsRow(friend, listOfBooks)
                                }
                            }
                        }
                    }
            }
        }

    }


}

@Composable
fun UserStatsRow(
    user: MUser,
    listOfBooks: List<MBook>) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 7.dp) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top) {

            Image(
                painter = rememberImagePainter(data = R.drawable.ic_baseline_menu_book_24),
                contentDescription = "book image",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp),
            )

            Column {
               Text(text = user.displayName, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.h6,
                   color = Constants.APP_COLOR
               )
                val readBooksList: List<MBook> = if(listOfBooks.isNotEmpty()){
                    listOfBooks.filter{ mBook ->
                        (mBook.userId == user.userId) && (mBook.finishedReading !=null)
                    }
                }else{
                    emptyList()
                }

                val readingBooks:List<MBook> = if(listOfBooks.isNotEmpty()) {
                    listOfBooks.filter{mbook ->
                        (mbook.startedReading != null && mbook.finishedReading ==null)
                    }
                }else{
                    emptyList()
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp
                ){
                    Column(modifier = Modifier.padding(start = 25.dp,top = 8.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.Start){
                        Text(text = "Friend's stats:", style = MaterialTheme.typography.h5, color = Constants.APP_COLOR)
                        Divider()
                        Text(text = "Reading: ${readingBooks.size} books")
                        Text(text = "Finished: ${readBooksList.size} books")

                    }
                }
            }
        }
    }
}