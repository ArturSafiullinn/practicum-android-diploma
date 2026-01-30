package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.util.ResourceProvider

val appModule = module {

    single { ResourceProvider(get()) }
    // View Models
    // Repos
    // Interactors
}
