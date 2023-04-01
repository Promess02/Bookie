package mikolaj.michalczyk.readerapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import mikolaj.michalczyk.readerapp.R
import mikolaj.michalczyk.readerapp.navigation.ReaderScreens
@Composable
fun ReaderAppBar(
    title: String = "Bookie",
    showProfile: Boolean = true,
    navController: NavController,
    onNavIconClick: () -> Unit = {}
){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_menu_book_24), contentDescription = "logo icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.8f)
                    )

                }
                Text(
                        text = title, color = Color.Red.copy(alpha = 0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Spacer(modifier = Modifier.width(150.dp))
            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreens.LoginScreen.name)
                }
            }) {
                if(showProfile){
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "logout"
                    )
                }
            }
        },
        backgroundColor = Color.White,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavIconClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open Navigation Drawer"
                )
            }
        }
            )

}

@Composable
fun ReaderAppBarAlt(
    icon: ImageVector? = null,
    title: String = "Bookie",
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked: () -> Unit = {},
){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_menu_book_24), contentDescription = "logo icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.6f)
                    )

                }
                if(icon!=null){
                    Icon(imageVector = icon, contentDescription = "arrow Back", tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.clickable{onBackArrowClicked.invoke()})
                }
                Text(
                    text = title, color = Color.Red.copy(alpha = 0.7f),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Spacer(modifier = Modifier.width(150.dp))
            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreens.LoginScreen.name)
                }
            }) {
                if(showProfile){
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "logout"
                    )
                }
            }
        },
        backgroundColor = Color.White,
    )

}