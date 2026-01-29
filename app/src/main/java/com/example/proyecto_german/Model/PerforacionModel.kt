package com.example.proyecto_german.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName ="Perforacion")
data class PerforacionModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val codigo:String,
    val fecha_revision: Date,
    val revision:String,
    val clinte:String,
    val atencion:String,
    val proyecto:String,
    val localizacion:String,
    val fecha: Date,
    val numero_perforacion: Int,
    val profundiad_metros: Double,
    val coordenada_e:Double,
    val coordenada_n: Double,
    val nivel_freatico: Boolean,
    val lectura_inicial: Double,
    val lectura_final: Double,
    val estado_del_tiempo:String
)