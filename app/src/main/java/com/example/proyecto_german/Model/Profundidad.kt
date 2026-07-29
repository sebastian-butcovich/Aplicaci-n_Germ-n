package com.example.proyecto_german.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Profundidades",
    foreignKeys = [
        ForeignKey(
            entity = Perforacion::class,
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
    val sucs: String,
    val descripcion: String,
    val simbolo: String,
    val profundidadInicial:Double?=0.0,
    val profundidadFinal:Double?=0.0
)
//enum class Sucs{
//    VACIO,
//    CH,
//    MH,
//    SM,
//    GP,
//}
