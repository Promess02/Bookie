package mikolaj.michalczyk.readerapp.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Favicon(
            size: Dp = 48.dp,
            isFavourite: Boolean =false,
            onPressFav: (Boolean) -> Unit

){
    var favState by remember {
        mutableStateOf(isFavourite)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val sizeState by animateDpAsState(
        targetValue = if (selected) size*1.5f else size,
        spring(Spring.DampingRatioMediumBouncy)
    )
    Icon(imageVector = Icons.Rounded.FavoriteBorder,
        contentDescription = "Fav Icon",
        modifier = Modifier
            .width(sizeState)
            .height(sizeState)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        selected = true
                        favState = !favState
                        onPressFav(favState)
                    }
                    MotionEvent.ACTION_UP -> {
                        selected = false
                    }
                }
                true
            },
        tint = if (favState) Color(0xFFC043BE) else Color(0xFF4D5A5F),

    )
}