package com.example.proyecto_german.Data.Application

import android.app.Application
import androidx.room.Room
import com.example.proyecto_german.Data.Database.PerforacionesDatabase

class PerforacionesApplication: Application() {
    companion object{
        lateinit var  database: PerforacionesDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this,PerforacionesDatabase::class.java,"PerforacionesDatabase")
            .build()
    }
}