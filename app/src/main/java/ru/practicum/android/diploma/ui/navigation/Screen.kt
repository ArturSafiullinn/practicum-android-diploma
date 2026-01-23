package ru.practicum.android.diploma.ui.navigation

import ru.practicum.android.diploma.R

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Main : Screen("main", "Главная", R.drawable.ic_main)
    object Favorites : Screen("favorites", "Избранное", R.drawable.ic_favorites)
    object Team : Screen("team", "Команда", R.drawable.ic_team)

}
