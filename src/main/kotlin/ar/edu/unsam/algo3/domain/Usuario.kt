package ar.edu.unsam.algo3.domain

data class Usuario(
    override val id: Int? = null,
    val nombre: String? = null,
    val apellido: String? = null,
    val password: String? = null,
    val fechaNacimiento: String? = null
): Entidad
