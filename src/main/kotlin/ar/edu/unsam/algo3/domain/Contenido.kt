package ar.edu.unsam.algo3.domain


data class Contenido(
    val id: Int?,
    val titulo: String,
    val velocidadPromedio: Double?,
    val puntajeMax: Double?,
    val puntajePromedio: Double?,
    val tipoContenido: String,
    val idUsuarioResponde: Int?,
    val idRespuesta: Int?
)
