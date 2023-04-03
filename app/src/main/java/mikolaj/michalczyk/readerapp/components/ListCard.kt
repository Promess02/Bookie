package mikolaj.michalczyk.readerapp.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import mikolaj.michalczyk.readerapp.model.MBook

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListCard(book:MBook, onPressDetails: (String) -> Unit = {}) {
    val context = LocalContext.current
    val resources = context.resources

    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card(shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }) {

        Column(modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start) {
            Row(horizontalArrangement = Arrangement.Center) {

                Image(painter = rememberImagePainter(data = book.photoUrl.toString()),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(160.dp)
                        .width(130.dp)
                        .padding(start = 10.dp, top = 13.dp))
                Spacer(modifier = Modifier.width(5.dp))

                Column(modifier = Modifier.padding(top = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .size(58.dp)
                        ,
                    tint = if (book.isFavourite!!) Color(0xFFC043BE) else Color(0xFF4D5A5F)
                    )
                    BookRating(score = book.rating!!)
                }

            }
            Text(text = book.title.toString(), modifier = Modifier.padding(top = 5.dp, bottom = 2.dp, start = 5.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)

            Text(text = book.authors.toString().replace(Regex("[\\[\\]]"), ""), modifier = Modifier.padding(top = 3.dp, start = 6.dp),
                style = MaterialTheme.typography.caption) }

        val isStartedReading = remember {
            mutableStateOf(false)
        }
        Row(horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom) {
            isStartedReading.value = book.startedReading != null


            RoundedButton(label = if (isStartedReading.value)  "Reading" else "Not Yet",
                radius = 70)

        }

        }
}



