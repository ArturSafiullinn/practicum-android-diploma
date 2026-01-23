package ru.practicum.android.diploma.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.ui.screens.FavoritesScreen
import ru.practicum.android.diploma.ui.screens.MainScreen
import ru.practicum.android.diploma.ui.screens.TeamScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Main.route) { MainScreen() }
        composable(Screen.Favorites.route) { FavoritesScreen() }
        composable(Screen.Team.route) { TeamScreen() }
    }
}
