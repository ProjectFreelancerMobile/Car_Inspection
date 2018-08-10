package com.car_inspection.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orhanobut.logger.Logger
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory @Inject constructor(
        private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}

