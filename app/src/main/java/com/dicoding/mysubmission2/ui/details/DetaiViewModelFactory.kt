package com.dicoding.mysubmission2.ui.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mysubmission2.data.di.Injection
import com.dicoding.mysubmission2.data.remote.repository.UserRepository

class DetaiViewModelFactory private constructor(private val userRepository: UserRepository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
    }

    companion object{
        @Volatile
        private var instance : DetaiViewModelFactory? = null
        fun getInstance(context: Context): DetaiViewModelFactory =
            instance ?: synchronized(this){
                instance ?: DetaiViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}