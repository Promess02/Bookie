package mikolaj.michalczyk.readerapp.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import mikolaj.michalczyk.readerapp.R
import mikolaj.michalczyk.readerapp.components.*
import mikolaj.michalczyk.readerapp.data.DataOrException
import mikolaj.michalczyk.readerapp.model.MBook
import mikolaj.michalczyk.readerapp.navigation.ReaderScreens
import mikolaj.michalczyk.readerapp.screens.home.HomeScreenViewModel
import mikolaj.michalczyk.readerapp.utils.Constants.APP_GRADIENT
import mikolaj.michalczyk.readerapp.utils.formatDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun UpdateScreen(navController: NavController, bookItemId: String,
                 viewModel: HomeScreenViewModel = hiltViewModel()) {

    Scaffold(topBar = {
        ReaderAppBarAlt(
            title = "Update Book",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {
        val bookInfo  = produceState<DataOrException<List<MBook>,Boolean,Exception>>(initialValue = DataOrException(data = emptyList(),true,Exception(""))){
            value = viewModel.data.value
        }.value
        
        Surface(modifier = Modifier
            .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .background(brush = Brush.verticalGradient(APP_GRADIENT))
                    .padding(top = 15.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Log.d("Info", "UpdateScreen: ${viewModel.data.value.data.toString()}")
                if(bookInfo.loading == true){
                    LinearProgressIndicator()
                    bookInfo.loading = false
                }else{
                       ShowBookUpdate(bookInfo = viewModel.data.value, bookItemId = bookItemId, navController = navController)
                    val bookToUpdate = viewModel.data.value.data?.first{ mBook ->
                        mBook.googleBookId == bookItemId
                    }!!
                    ShowSimpleForm(book = bookToUpdate, navController)
                   }
            }
        }


    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowSimpleForm(book: MBook, navController: NavController) {
    val context = LocalContext.current
    val notesText = remember{
        mutableStateOf("")
    }

    val isStartedReading = remember{
        mutableStateOf(false)
    }

    val isFinishedReading = remember{
        mutableStateOf(false)
    }

    val ratingVal = remember{
        mutableStateOf(0)
    }

    val favState = remember{
        mutableStateOf(book.isFavourite)
    }
    SimpleForm(
        defaultValue = book.notes.toString().ifEmpty { "No thoughts available" }
    ){ note ->
        notesText.value = note

    }

    Row(modifier = Modifier.padding(4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start
    ){
        TextButton(onClick ={isStartedReading.value=true}, enabled = book.startedReading == null){
            if(book.startedReading ==null){
                if(!isStartedReading.value){
                    Text(text = "Start Reading!")
                }
                else{
                    Text(text = "Started Reading!: ",
                    modifier = Modifier.alpha(0.6f),
                    color = Color.Red.copy(alpha=0.5f))
                }
            }
            else{
                Text(text = "Started on: ${formatDate(book.startedReading!!)}") //format date
            }

        }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = { isFinishedReading.value = true}, enabled = book.finishedReading == null) {
            if(book.finishedReading == null){
                if(!isFinishedReading.value){
                    Text(text = "Mark as Read")
                }else{
                    Text(text = "Finished Reading!",modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha=0.5f))
                }
            }else{
                Text(text = "Finished on: ${formatDate(book.finishedReading!!)}")
            }

        }

    }
    Row(horizontalArrangement = Arrangement.SpaceBetween){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp), fontWeight = FontWeight.SemiBold)
            book.rating?.toInt().let { ratingNum ->
                RatingBar(rating = ratingNum!!) { rating ->
                    ratingVal.value = rating
                }
                Favicon(isFavourite = book.isFavourite!!){ isFav ->
                    favState.value = isFav
                }
            }
        }
    }


    Spacer(modifier = Modifier.padding(bottom = 20.dp))

    Row(modifier = Modifier.padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start) {
        val changedNotes = book.notes!= notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value
        val changedFav = book.isFavourite != favState.value
        val isFinishedTimeStamp = if(isFinishedReading.value) Timestamp.now() else book.finishedReading
        val isStartedTimestamp = if(isStartedReading.value) Timestamp.now() else book.startedReading

        val bookUpdate = changedNotes || changedRating || isStartedReading.value || isFinishedReading.value || changedFav

        val bookToUpdate = hashMapOf(
            "finished_reading" to isFinishedTimeStamp,
            "started_reading" to isStartedTimestamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value,
            "favourite" to favState.value
        ).toMap()

        RoundedButton(label = "Update", radius = 20, enabled = true){
           if(bookUpdate){

               Log.d("Info", "UpdateScreen: ${book.id}")
               Log.d("Info", "UpdateScreen: ${bookToUpdate.values}")
               FirebaseFirestore.getInstance()
                   .collection("books")
                   .document(book.id!!)
                   .update(bookToUpdate)
                   .addOnCompleteListener {
                       showToast(context, "Book Updated Successfully!")
                       Log.d("Info", "UpdateScreen: ${bookToUpdate.values}")
                   }
                   .addOnFailureListener {
                       Log.w("Error", "ShowSimpleForm: Error updating doc")
                   }
           }
           navController.navigate(ReaderScreens.ReaderHomeScreen.name)

       }
        Spacer(modifier = Modifier.width(80.dp))
        val openDialog = remember{
            mutableStateOf(false)
        }

        if(openDialog.value){
            ShowAlertDialog(message = stringResource(id = R.string.sure) + "\n" +
                    stringResource(id = R.string.action), openDialog){
                FirebaseFirestore.getInstance().collection("books")
                    .document(book.id!!)
                    .delete()
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            openDialog.value = false
                            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                        }
                    }
            }
        }
        RoundedButton(label = "Delete",radius =20, enabled = true){
            openDialog.value = true
        }

    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    defaultValue: String = "Great Book! ",
    onSearch: (String) -> Unit
) {
    Column {
        val textFieldValue = rememberSaveable{ mutableStateOf(defaultValue)}
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value){
            textFieldValue.value.trim().isNotEmpty()
        }
        
        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(White, RoundedCornerShape(15))
                .padding(horizontal = 8.dp, vertical = 5.dp),

            valueState = textFieldValue, labelId = "Enter your Thoughts",
        enabled = true, onAction = KeyboardActions{
                 if(!valid) return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                keyboardController?.hide()
            }
        )

    }

}

@Composable
fun ShowBookUpdate(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String,
    navController: NavController
) {
    Row {
        Spacer(modifier = Modifier.width(43.dp))
        if(bookInfo.data != null){
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center){
                val theBook = bookInfo.data!!.first{ mBook ->
                    mBook.googleBookId == bookItemId
                }
                CardListItem(book = theBook){
                    navController.navigate(ReaderScreens.DetailScreen.name + "/${theBook.id}")
                }
                ShowBookInfo(book = theBook)
            }
        }
        
    }

}

@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {
            Card(shape = RoundedCornerShape(10)) {
                Image(painter = rememberImagePainter(data = book.photoUrl.toString()), contentDescription = null,
                    modifier = Modifier
                        .height(190.dp)
                        .width(156.dp)
                        .padding(4.dp)
                        .clip(
                            RoundedCornerShape(
                                20.dp
                            )
                        )
                        .clickable { onPressDetails() },
                    alignment = Alignment.Center)
            }

}

@Composable
fun ShowBookInfo(book: MBook){
    Column{
        Text(text = book.title.toString(),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp),
            fontWeight = FontWeight.SemiBold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(text = book.publishedData.toString(),
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 0.dp))

    }
}
