package ar.edu.unsam.algo3.domain

data class Descarga(
    val id: Int,
    val velocidad: Double,
    val idUsuario: Int,
    val idContenidoMusica: Int?,
    val idContenidoDocumento: Int?
)
