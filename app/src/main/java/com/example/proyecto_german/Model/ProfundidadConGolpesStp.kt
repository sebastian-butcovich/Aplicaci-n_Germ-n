package com.example.proyecto_german.Model

import androidx.room.Embedded
import androidx.room.Relation

data class ProfundidadConGolpesStp(
    @Embedded
    val profundidad: Profundidad,
    @Relation(
        entity = Profundidad::class,
        parentColumn = "id",
        entityColumn = "profundidadId"
    )
    val golpesStp: GolpesStp
)
