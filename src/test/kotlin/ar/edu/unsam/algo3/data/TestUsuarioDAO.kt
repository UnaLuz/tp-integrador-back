package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.utils.Logger

const val TAG = "TestUsuarioDAO"

fun main(args: Array<String>) {
    val id = 2
    val usuarioDAO = UsuarioRepository()

    Logger.info(TAG, "--- Usuario con id=$id ---")
    Logger.info(TAG, usuarioDAO.selectOne(Usuario(id = id)).toString())

    Logger.info(TAG, "--- Select all Usuarios ---")
    usuarioDAO.selectAll()?.forEach { usuario ->
        Logger.info(TAG, usuario.toString())
    } ?: Logger.warning(TAG, "La lista de usuarios no pudo ser recuperada")
}