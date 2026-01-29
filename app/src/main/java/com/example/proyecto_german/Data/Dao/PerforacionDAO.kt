package com.example.proyecto_german.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.proyecto_german.Model.PerforacionConProfundidadesModel
import com.example.proyecto_german.Model.PerforacionModel

@Dao
interface PerforacionDAO {
    @Query("SELECT * FROM Perforacion")
    suspend fun getAllPerforaciones(): MutableList<PerforacionModel>
    @Insert
    fun agregarPerforacion(perforacionModel: PerforacionModel): Long
  @Transaction
  @Query("SELECT * FROM Perforacion")
  fun getUsersWithPlaylists(): List<PerforacionConProfundidadesModel>
}