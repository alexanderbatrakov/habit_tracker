package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.core.database.AppDatabase
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
    ]
)
interface AppComponent {

    fun viewModelFactory(): DaggerViewModelFactory

    fun database(): AppDatabase

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
