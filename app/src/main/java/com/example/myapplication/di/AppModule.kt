package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.BuildConfig
import com.example.myapplication.core.database.AppDatabase
import com.example.myapplication.core.database.dao.HabitRecordsDao
import com.example.myapplication.core.database.dao.HabitsDao
import com.example.myapplication.core.network.wger.WgerApi
import com.example.myapplication.domain.TimeProvider
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
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

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
                )
            }
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl("https://wger.de/")
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideWgerApi(retrofit: Retrofit): WgerApi = retrofit.create(WgerApi::class.java)
}
