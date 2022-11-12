package ar.edu.unsam.algo3.domain

data class Encuesta(
    val id: Int?,
    val resumenPositivo: String,
    val resumenNegativo: String,
    val puntaje: Double,
)