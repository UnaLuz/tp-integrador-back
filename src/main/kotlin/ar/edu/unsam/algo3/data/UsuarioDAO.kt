package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.Usuario
import java.sql.ResultSet

class UsuarioDAO(
    val entidadDAO: EntidadDAO<Usuario> = EntidadDAO()
) {
    companion object {
        // Table info
        const val DB_TABLE = "usuario"
        const val COL_ID_USUARIO = "id_usuario"
        const val COL_NOMBRE = "nombre"
        const val COL_APELLIDO = "apellido"
        const val COL_PASSWORD = "password"
        const val COL_FECHA_NACIMIENTO = "fecha_nacimiento"
    }

    // Queries
    private val SELECT =
        "SELECT $COL_ID_USUARIO, $COL_NOMBRE, $COL_APELLIDO, $COL_PASSWORD, $COL_FECHA_NACIMIENTO FROM $DB_TABLE;"

    private val SELECT_ONE =
        "SELECT $COL_ID_USUARIO, $COL_NOMBRE, $COL_APELLIDO, $COL_PASSWORD, $COL_FECHA_NACIMIENTO FROM $DB_TABLE WHERE $COL_ID_USUARIO = ?;"

    fun selectAll(): List<Usuario>? =
        entidadDAO.selectAll(SELECT) { it.mapToUsuario() }

    fun selectOne(usuario: Usuario): Usuario? =
        entidadDAO.selectOne(SELECT_ONE, { stmt ->
            usuario.id?.let { stmt.setInt(1, it) }
        }) { it.mapToUsuario() }

}

fun ResultSet.mapToUsuario(): Usuario =
    Usuario(
        id = getInt(UsuarioDAO.COL_ID_USUARIO),
        nombre = getString(UsuarioDAO.COL_NOMBRE),
        apellido = getString(UsuarioDAO.COL_APELLIDO),
        password = getString(UsuarioDAO.COL_PASSWORD),
        fechaNacimiento = getString(UsuarioDAO.COL_FECHA_NACIMIENTO)
    )