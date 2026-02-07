package com.example.proyecto_german.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_german.Repository.PerforacionRepository

class PeforacionViewModelFactory(private val respository: PerforacionRepository): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        if(modelClass.isAssignableFrom(PerforacionViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return PerforacionViewModel(respository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}