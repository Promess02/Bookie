package mikolaj.michalczyk.readerapp.navigation

import com.google.common.math.Stats
import mikolaj.michalczyk.readerapp.screens.stats.details.BookDetailsScreen

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    ReaderHomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    FavouriteScreen,
    StatsScreen;
    companion object{
        fun fromRoute(route:String): ReaderScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            DetailScreen.name -> DetailScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            SearchScreen.name -> SearchScreen
            FavouriteScreen.name -> FavouriteScreen
            null -> ReaderHomeScreen
            else -> throw IllegalArgumentException("Route $route is recognized")
        }
    }
}