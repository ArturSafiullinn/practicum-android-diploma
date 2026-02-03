package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity

@Database(
    entities = [VacancyDetailEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacancyDao(): VacancyDetailDao
}
