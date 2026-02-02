package com.example.proyecto_german.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.proyecto_german.Data.Dao.PerforacionDAO
import com.example.proyecto_german.Data.DataConverters
import com.example.proyecto_german.Model.PerforacionConProfundidadesModel
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad

@Database(
    entities = [PerforacionModel::class, Profundidad::class, PerforacionConProfundidadesModel::class]
    , version = 1,
    exportSchema = false
)
@TypeConverters(DataConverters::class)
abstract  class PerforacionesDatabase: RoomDatabase() {
    abstract fun perforacionDao(): PerforacionDAO

}