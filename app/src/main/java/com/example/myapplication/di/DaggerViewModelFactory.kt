package com.example.myapplication.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class DaggerViewModelFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = providers[modelClass]
            ?: providers.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: error("No ViewModel binding for $modelClass")
        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}
