package mikolaj.michalczyk.readerapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mikolaj.michalczyk.readerapp.R

@Preview
@Composable
fun BookRating(score: Double = 4.5) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .width(50.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(40.dp),
        elevation = 3.dp,
        color = Color.White,
    ) {
        Column(modifier = Modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)
            Icon(
                modifier = Modifier.width(25.dp).height(25.dp),
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
            )
        }

    }

}