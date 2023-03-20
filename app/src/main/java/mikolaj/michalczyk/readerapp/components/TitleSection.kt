package mikolaj.michalczyk.readerapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleSection(modifier: Modifier = Modifier,
                 label: String){

        Surface(
            modifier = Modifier
                .padding(start = 15.dp, top = 5.dp)
                .clip(
                    RoundedCornerShape(
                        bottomEndPercent = 25,
                        topStartPercent = 25
                    )
                ),
            color = Color.White
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = label,
                    style = TextStyle(color = Color(0xFFA872C5)),
                    fontSize = 22.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(5.dp)
                )
            }

        }

    }
