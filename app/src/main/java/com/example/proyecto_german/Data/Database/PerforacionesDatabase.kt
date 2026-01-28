package com.example.proyecto_german.Data.Database

//import androidx.room.RoomDatabase
import com.example.proyecto_german.Data.Dao.PerforacionDAO

abstract  class PerforacionesDatabase/*: RoomDatabase()*/ {
    abstract fun perforacionDao(): PerforacionDAO
}