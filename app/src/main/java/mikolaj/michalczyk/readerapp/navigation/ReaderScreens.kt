package mikolaj.michalczyk.readerapp.navigation

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    ReaderHomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    FavouriteScreen,
    StatsScreen,
    FriendsScreen;
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
            FriendsScreen.name -> FriendsScreen
            null -> ReaderHomeScreen
            else -> throw IllegalArgumentException("Route $route is recognized")
        }
    }
}