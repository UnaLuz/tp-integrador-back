package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.data.UsuarioDAO
import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.utils.Logger

const val TAG = "TestDatabaseConnection"

fun main(args: Array<String>) {

    Logger.debug(TAG, "--- Insert Usuario ---")
    val resultado = UsuarioDAO.insert(
        Usuario(
            nombre = "Pepita",
            apellido = null,
            password = "",
            fechaNacimiento = "2022-11-10"
        )
    )
    Logger.debug(TAG, "Se insertaron $resultado usuario/s")

    Logger.debug(TAG, "--- Select Usuarios ---")
    UsuarioDAO.selectAll()?.forEach { usuario ->
        Logger.debug(TAG, usuario.toString())
    } ?: Logger.debug(TAG, "La lista de usuarios no pudo ser recuperada")
}