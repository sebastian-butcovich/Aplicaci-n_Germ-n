package com.example.proyecto_german.Model.Temporales

import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.Model.Profundidad

data class ProfundidadConGolpes(
    val profundidad: Profundidad,
    val golpes:List<GolpesStp>
)
