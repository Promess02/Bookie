package mikolaj.michalczyk.readerapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import mikolaj.michalczyk.readerapp.R

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        text = "Bookie", style = MaterialTheme.typography.h3,
        color = Color.Red.copy(alpha = 0.5f),
        modifier = Modifier.padding(bottom = 16.dp),
        fontFamily = FontFamily.Serif
    )
    Image(painter = painterResource(id = R.drawable.ic_baseline_menu_book_24), contentDescription = "book icon",
        modifier = Modifier.scale(3.0f))
}