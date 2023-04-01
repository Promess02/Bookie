package mikolaj.michalczyk.readerapp.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import mikolaj.michalczyk.readerapp.components.ReaderLogo
import mikolaj.michalczyk.readerapp.navigation.ReaderScreens
import mikolaj.michalczyk.readerapp.utils.Constants.APP_COLOR
import mikolaj.michalczyk.readerapp.utils.Constants.QUOTES_LIST


@Composable
fun ReaderSplashScreen(navController: NavController){

    // animation
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true){
        scale.animateTo(targetValue = 0.9f,
        animationSpec = tween(durationMillis = 800,
        easing = {
            OvershootInterpolator(8f)
                .getInterpolation(it)
        }))
        delay(2000L)
        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate(ReaderScreens.LoginScreen.name)
        }else{
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }

    }
    // animation
    Surface(modifier = Modifier
        .padding(15.dp)
        .size(330.dp)
        .scale(scale.value), // scale the surface by mutable scale controlled by animation
    shape = CircleShape,
    color = Color.White,
        border = BorderStroke(width = 3.dp,color = APP_COLOR),
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReaderLogo(modifier = Modifier.height(50.dp))
            Spacer(modifier = Modifier.height(25.dp))
            Row(modifier = Modifier.padding(15.dp)) {
                Text(text = "\"${QUOTES_LIST.shuffled()[0]}\"",
                    style = MaterialTheme.typography.h6, color=Color.DarkGray, textAlign = TextAlign.Center, fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.SansSerif)
            }
        }
    }
}

@Preview
@Composable
fun ReaderSplashScreenPreview(){
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true){
        scale.animateTo(targetValue = 0.9f,
            animationSpec = tween(durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }))
        delay(2000L)

    }
    // animation
    Surface(modifier = Modifier
        .padding(15.dp)
        .size(320.dp)
        .scale(scale.value), // scale the surface by mutable scale controlled by animation
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 3.dp,color = APP_COLOR),
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReaderLogo(modifier = Modifier.height(50.dp))
            Spacer(modifier = Modifier.height(25.dp))
            Row(modifier = Modifier.padding(15.dp)) {
                Text(text = "\"${QUOTES_LIST.shuffled()[0]}\"",
                    style = MaterialTheme.typography.h6, color=Color.DarkGray, textAlign = TextAlign.Center, fontWeight = FontWeight.Light,
                fontFamily = FontFamily.SansSerif)
            }
        }
    }
}

