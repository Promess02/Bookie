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
import mikolaj.michalczyk.readerapp.screens.favourite.FavouriteScreen
import mikolaj.michalczyk.readerapp.screens.details.BookDetailsScreen
import mikolaj.michalczyk.readerapp.screens.friends.ReaderFriendsScreen
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
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(ReaderScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }
        composable(ReaderScreens.StatsScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            StatsUpdateScreen(navController=navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.FavouriteScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            FavouriteScreen(navController=navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.UpdateScreen.name){
            UpdateScreen(navController=navController, bookItemId = it.toString())
        }
        composable(ReaderScreens.LoginScreen.name){
            LoginScreen(navController=navController)
        }
        composable(ReaderScreens.FriendsScreen.name){
            ReaderFriendsScreen(navController = navController)
        }
        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){ backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let{
                BookDetailsScreen(navController=navController, bookId = it.toString())
            }
        }
        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId"){
                type = NavType.StringType
            })){
            navBackStackEntry ->
            navBackStackEntry.arguments?.getString("bookItemId").let{
                UpdateScreen(navController = navController, bookItemId = it.toString())
            }
        }

    }
}