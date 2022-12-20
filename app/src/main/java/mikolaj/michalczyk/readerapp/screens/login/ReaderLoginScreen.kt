package mikolaj.michalczyk.readerapp.screens.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mikolaj.michalczyk.readerapp.components.ReaderLogo
import mikolaj.michalczyk.readerapp.components.UserForm
import mikolaj.michalczyk.readerapp.navigation.ReaderScreens

@Composable
fun LoginScreen(navController: NavController,
                viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    val showLoginForm = rememberSaveable{
        mutableStateOf(true)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
            ReaderLogo()

            if(showLoginForm.value) UserForm(loading = false, isCreateAccount = false){
                email, password ->
                viewModel.signInWithEmailAndPassword(email,password){
                    navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                }
            } else{
                UserForm(loading = false, isCreateAccount = true){email,password ->
                    viewModel.createUserWithEmailAndPassword(email,password){
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
                val text = if(showLoginForm.value) "Sign up" else "Login"
                Text(text = "New User?")
                Text(text,
                modifier = Modifier
                    .clickable {
                        showLoginForm.value = !showLoginForm.value
                    }
                    .padding(start = 5.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondaryVariant)
            }
        }
    }
}
