package com.example.proyecto_german.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName ="perforaciones")
data class PerforacionModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val codigo:String,
    val fechaRevision: Date,
    val revision:String,
    val clinte:String,
    val atencion:String,
    val proyecto:String,
    val localizacion:String,
    val fecha: Date,
    val numeroPerforacion: Int,
    val profundidadMetros: Double,
    val coordenadaE:Double,
    val coordenadaN: Double,
    val nivelFreatico: Boolean,
    val lecturaInicial: Double,
    val lecturaFinal: Double,
    val estadoDelTiempo:String
)