package mikolaj.michalczyk.readerapp.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun nullText(){
    Text(text = "null",
        overflow = TextOverflow.Clip,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.caption)
}