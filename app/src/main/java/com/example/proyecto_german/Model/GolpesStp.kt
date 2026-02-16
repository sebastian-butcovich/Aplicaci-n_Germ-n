package com.example.proyecto_german.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "golpesStp")
data class GolpesStp(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profundidadId: Long,
    val profundidad_inicial: Double,
    val profundidad_final: Double,
    val numero_muestra: Double,
    val tipo: String,
    val golpes1: Int,
    val golpes2: Int,
    val golpes3: Int,
)