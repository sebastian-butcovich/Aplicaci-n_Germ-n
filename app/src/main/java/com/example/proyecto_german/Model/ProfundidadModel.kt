package com.example.proyecto_german.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profundidades")
data class Profundidad (
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val perforacionId:Long,
    val profundidad: Double,
    val numero_muestra: Int,
    val tipo: String,
    val golpes1: Int,
    val golpes2: Int,
    val golpes3: Int,
    val sucs: Sucs,
    val descripcion: String,
    val nf: Double,
)
enum class Sucs{
    CH,
    MH,
    SM,
    GP,
}