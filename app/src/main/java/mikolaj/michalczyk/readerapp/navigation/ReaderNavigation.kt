package mikolaj.michalczyk.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mikolaj.michalczyk.readerapp.screens.ReaderSplashScreen
import mikolaj.michalczyk.readerapp.screens.details.BookDetailsScreen
import mikolaj.michalczyk.readerapp.screens.home.HomeScreen
import mikolaj.michalczyk.readerapp.screens.home.HomeScreenViewModel
import mikolaj.michalczyk.readerapp.screens.login.LoginScreen
import mikolaj.michalczyk.readerapp.screens.search.SearchScreen
import mikolaj.michalczyk.readerapp.screens.stats.StatsUpdateScreen
import mikolaj.michalczyk.readerapp.screens.update.UpdateScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name){
        composable(ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name){
            val homeViewMOdel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = homeViewMOdel)
        }
        composable(ReaderScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }
        composable(ReaderScreens.StatsScreen.name){

            StatsUpdateScreen(navController=navController)
        }
        composable(ReaderScreens.UpdateScreen.name){
            UpdateScreen(navController=navController)
        }
        composable(ReaderScreens.LoginScreen.name){
            LoginScreen(navController=navController)
        }
        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){ backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let{
                BookDetailsScreen(navController=navController, bookId = it.toString())
            }
        }

    }
}