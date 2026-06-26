package com.example.proyecto_german.Model

data class Cliente(
    val nombre:String,
    val email:String,
    val telefono:String,
    val direccion: Direccion,
    val proyectos: List<Proyecto>
)
