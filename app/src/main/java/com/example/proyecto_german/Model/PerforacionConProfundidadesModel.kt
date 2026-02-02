package com.example.proyecto_german.Model

import androidx.room.Embedded
import androidx.room.Relation

data class PerforacionConProfundidadesModel(
    @Embedded
    val perforacion: PerforacionModel,
    @Relation(
       parentColumn = "id",
       entityColumn = "perforacionId"
   )
    val profundidad:List<Profundidad>
)