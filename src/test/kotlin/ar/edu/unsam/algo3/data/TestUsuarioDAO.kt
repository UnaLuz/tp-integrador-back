package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.utils.Logger

const val TAG = "TestUsuarioDAO"

fun main(args: Array<String>) {
    val id = 18
    Logger.info(TAG, "--- Insert Usuario ---")
    val resultado = UsuarioDAO.insert(
        Usuario(
            nombre = "Manu",
            apellido = null,
            password = "asd",
            fechaNacimiento = "2022-11-11"
        )
    )
    Logger.debug(TAG, "Se insertaron $resultado usuario/s")

    Logger.info(TAG, "--- Usuario con id=$id antes del update ---")
    Logger.info(TAG, UsuarioDAO.selectOne(Usuario(id = id)).toString())

    Logger.info(TAG, "--- Update Usuario ---")
    val actualizados = UsuarioDAO.update(
        Usuario(
            id = id,
            nombre = "Andres",
            apellido = "ccc",
            password = "qwerty",
            fechaNacimiento = "2022-11-11"
        )
    )
    Logger.debug(TAG, "Se actualizaron $actualizados usuario/s")

    Logger.info(TAG, "--- Usuario con id=$id despues del update---")
    Logger.info(TAG, UsuarioDAO.selectOne(Usuario(id = id)).toString())

    Logger.info(TAG, "--- Delete Usuario ---")
    Logger.info(TAG, "Eliminando usuario con id=$id")
    val eliminados = UsuarioDAO.delete(Usuario(id = id))
    Logger.debug(TAG, "Se eliminaron $eliminados usuario/s")

    Logger.info(TAG, "--- Select Usuarios ---")
    UsuarioDAO.selectAll()?.forEach { usuario ->
        Logger.info(TAG, usuario.toString())
    } ?: Logger.warning(TAG, "La lista de usuarios no pudo ser recuperada")
}