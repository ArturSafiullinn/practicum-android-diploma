package ru.practicum.android.diploma.util

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.appModule
import ru.practicum.android.diploma.di.networkModule
import ru.practicum.android.diploma.di.storageModule
import ru.practicum.android.diploma.domain.api.ThemeInteractor

class App : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                networkModule,
                storageModule
            )
        }

        val themeInteractor: ThemeInteractor = get()
        applyTheme(themeInteractor.isDarkTheme())
    }
}
