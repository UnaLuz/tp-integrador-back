package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.Usuario
import java.sql.ResultSet

object UsuarioDAO {
    private val TAG = this::class.simpleName.toString()

    // DB info
    const val DB_TABLE = "usuario"
    const val COL_ID_USUARIO = "id_usuario"
    const val COL_NOMBRE = "nombre"
    const val COL_APELLIDO = "apellido"
    const val COL_PASSWORD = "password"
    const val COL_FECHA_NACIMIENTO = "fecha_nacimiento"

    // Queries
    private const val SELECT =
        "SELECT $COL_ID_USUARIO, $COL_NOMBRE, $COL_APELLIDO, $COL_PASSWORD, $COL_FECHA_NACIMIENTO FROM $DB_TABLE;"
    private const val INSERT =
        "INSERT INTO $DB_TABLE($COL_NOMBRE, $COL_APELLIDO, $COL_PASSWORD, $COL_FECHA_NACIMIENTO) VALUES(?, ?, ?, ?);"
    private const val UPDATE =
        "UPDATE $DB_TABLE SET $COL_NOMBRE = ?, $COL_APELLIDO = ?, $COL_PASSWORD = ?, $COL_FECHA_NACIMIENTO = ? WHERE $COL_ID_USUARIO = ?;"
    private const val DELETE =
        "DELETE FROM $DB_TABLE WHERE $COL_ID_USUARIO = ?;"
    private const val SELECT_ONE =
        "SELECT $COL_ID_USUARIO, $COL_NOMBRE, $COL_APELLIDO, $COL_PASSWORD, $COL_FECHA_NACIMIENTO FROM $DB_TABLE WHERE $COL_ID_USUARIO = ?;"

    // Errores
    const val DB_ERROR = -1
    const val USER_ERROR = -2

    /**
     * Realiza un select de todos los usuarios de la base de datos.
     *
     * @return Una lista de usuarios o null si ocurrió un error.
     */
    fun selectAll(): List<Usuario>? =
        DBConnection.executeQuery(SELECT) { resultSet ->
            resultSet.mapToList {
                it.mapToUsuario()
            }
        }

    /**
     * Inserta el usuario recibido en la base de datos.
     *
     * El campo **id** es ignorado ya que es seteado por la base de datos.
     *
     * Si alguno de los campos es nulo, se utiliza un string vacío.
     */
    fun insert(usuario: Usuario): Int =
        DBConnection.executeUpdate(INSERT) {
            it.setString(1, usuario.nombre.orEmpty())
            it.setString(2, usuario.apellido.orEmpty())
            it.setString(3, usuario.password.orEmpty())
            it.setString(4, usuario.fechaNacimiento.orEmpty())
        } ?: DB_ERROR

    fun update(usuario: Usuario): Int {
        return usuario.id?.let { id ->
            DBConnection.executeUpdate(UPDATE) {
                it.setString(1, usuario.nombre.orEmpty())
                it.setString(2, usuario.apellido.orEmpty())
                it.setString(3, usuario.password.orEmpty())
                it.setString(4, usuario.fechaNacimiento.orEmpty())
                it.setInt(5, id)
            } ?: DB_ERROR
        } ?: USER_ERROR
    }

    fun delete(usuario: Usuario): Int {
        return usuario.id?.let { id ->
            DBConnection.executeUpdate(DELETE) {
                it.setInt(1, id)
            } ?: DB_ERROR
        } ?: USER_ERROR
    }

    fun selectOne(usuario: Usuario): Usuario? {
        return usuario.id?.let { id ->
            DBConnection.executeQuery(SELECT_ONE, {
                it.setInt(1, id)
            }) { resultSet ->
                if(resultSet.next()) resultSet.mapToUsuario() else null
            }
        }
    }
}

private fun ResultSet.mapToUsuario() =
    Usuario(
        id = getInt(UsuarioDAO.COL_ID_USUARIO),
        nombre = getString(UsuarioDAO.COL_NOMBRE),
        apellido = getString(UsuarioDAO.COL_APELLIDO),
        password = getString(UsuarioDAO.COL_PASSWORD),
        fechaNacimiento = getString(UsuarioDAO.COL_FECHA_NACIMIENTO)
    )
