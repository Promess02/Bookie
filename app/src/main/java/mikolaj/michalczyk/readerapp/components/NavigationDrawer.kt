package mikolaj.michalczyk.readerapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import mikolaj.michalczyk.readerapp.R

@Preview
@Composable
fun DrawerContent(
    gradientColors: List<Color> = listOf(Color(0xFFBA68C4), Color(0xFF123566)),
    itemClick: (String) -> Unit = {}
) {

    val itemsList = prepareNavigationDrawerItems()
    val user = FirebaseAuth.getInstance().currentUser
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = gradientColors)),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 36.dp)
    ) {

        item {

             //user's image
            Image(
                modifier = Modifier
                    .size(size = 120.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Profile Image"
            )

            // user's name
            Text(
                modifier = Modifier
                    .padding(top = 12.dp),
                text = user?.email.toString(),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // user's email
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 30.dp),
                text = user?.email.toString(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        items(itemsList) { item ->
            NavigationListItem(item = item) {
                itemClick(item.label)
            }
        }
    }
}

@Composable
private fun NavigationListItem(
    item: NavigationDrawerItem,
    itemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                itemClick()
            }
            .padding(horizontal = 24.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // icon and unread bubble
        Box {
            Icon(
                modifier = Modifier
                    .padding(all = 2.dp)
                    .size(size = 28.dp),
                painter = item.image,
                contentDescription = null,
                tint = Color.White
            )

        }

        // label
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = item.label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
private fun prepareNavigationDrawerItems(): List<NavigationDrawerItem> {
    val itemsList = arrayListOf<NavigationDrawerItem>()

    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.ic_home),
            label = "Home"
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.message_square),
            label = "Messages",
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.bell),
            label = "Notifications",
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.user),
            label = "Profile"
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.settings),
            label = "Settings"
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.log_out),
            label = "Logout"
        )
    )
    return itemsList
}

data class NavigationDrawerItem(
    val image: Painter,
    val label: String,
)