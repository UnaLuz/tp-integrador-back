package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.Usuario
import java.sql.ResultSet

object UsuarioDAO {
    private val TAG = this::class.simpleName.toString()
    private const val SELECT = "SELECT id_usuario, nombre, apellido, password, fecha_nacimiento FROM usuario;"
    private const val INSERT =
        "INSERT INTO usuario(nombre, apellido, password, fecha_nacimiento) VALUES(?, ?, ?, ?);"
    const val DB_ERROR = -1

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
        DBConnection.executeUpdate(
            INSERT
        ) {
            it.setString(1, usuario.nombre.orEmpty())
            it.setString(2, usuario.apellido.orEmpty())
            it.setString(3, usuario.password.orEmpty())
            it.setString(4, usuario.fechaNacimiento.orEmpty())
        } ?: DB_ERROR
}

private fun ResultSet.mapToUsuario() =
    Usuario(
        id = getInt("id_usuario"),
        nombre = getString("nombre"),
        apellido = getString("apellido"),
        password = getString("password"),
        fechaNacimiento = getString("fecha_nacimiento")
    )
