package mikolaj.michalczyk.readerapp.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mikolaj.michalczyk.readerapp.utils.Constants.APP_COLOR

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() },
        shape = RoundedCornerShape(50.dp),
        backgroundColor = APP_COLOR
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = "add",
            tint = Color.White
        )
    }
    }