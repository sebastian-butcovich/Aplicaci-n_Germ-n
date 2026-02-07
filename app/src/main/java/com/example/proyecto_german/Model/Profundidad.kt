package com.example.proyecto_german.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Profundidades",
    foreignKeys = [
        ForeignKey(
            entity = PerforacionModel::class,
            parentColumns = ["id"],
            childColumns = ["perforacionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("perforacionId")]
)
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
   // val nf: Double,
)
enum class Sucs{
    CH,
    MH,
    SM,
    GP,
}