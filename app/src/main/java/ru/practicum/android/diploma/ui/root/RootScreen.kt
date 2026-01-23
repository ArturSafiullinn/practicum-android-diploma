package ru.practicum.android.diploma.ui.root


import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.ui.navigation.AppNavGraph
import ru.practicum.android.diploma.ui.navigation.Screen

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Main, Screen.Favorites, Screen.Team)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        label = { Text(screen.title) },
                        icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    // Очищаем стек до стартового экрана, чтобы не копить вкладки
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavGraph(navController = navController, paddingValues = innerPadding)
    }
}
