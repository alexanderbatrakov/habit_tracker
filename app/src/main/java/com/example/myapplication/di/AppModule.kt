package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.dao.HabitRecordsDao
import com.example.myapplication.data.local.dao.HabitsDao
import com.example.myapplication.domain.TimeProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "habits.db",
        ).fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideHabitsDao(db: AppDatabase): HabitsDao = db.habitsDao()

    @Provides
    fun provideHabitRecordsDao(db: AppDatabase): HabitRecordsDao = db.habitRecordsDao()

    @Provides
    @Singleton
    fun provideTimeProvider(): TimeProvider = object : TimeProvider {
        override fun nowMillis(): Long = System.currentTimeMillis()
    }
}
