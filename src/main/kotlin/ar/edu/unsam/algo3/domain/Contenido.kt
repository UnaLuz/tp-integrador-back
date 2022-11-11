package ar.edu.unsam.algo3.domain

data class Contenido(
    override val id: Int? = null,
    val titulo: String? = null,
    val extension: String? = null,
    val fechaPublicacion: String? = null,
    val tipoContenido: String? = null
) : Entidad
