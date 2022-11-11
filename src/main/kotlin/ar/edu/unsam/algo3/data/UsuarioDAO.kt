package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.Usuario
import java.sql.PreparedStatement
import java.sql.ResultSet

object UsuarioDAO : EntidadDAO<Usuario> {
    private val TAG = this::class.simpleName.toString()

    // Table info
    override val DB_TABLE = "usuario"
    const val COL_ID_USUARIO = "id_usuario"
    const val COL_NOMBRE = "nombre"
    const val COL_APELLIDO = "apellido"
    const val COL_PASSWORD = "password"
    const val COL_FECHA_NACIMIENTO = "fecha_nacimiento"

    // Queries
    override val SELECT =
        "SELECT $COL_ID_USUARIO, $COL_NOMBRE, $COL_APELLIDO, $COL_PASSWORD, $COL_FECHA_NACIMIENTO FROM $DB_TABLE;"
    override val INSERT =
        "INSERT INTO $DB_TABLE($COL_NOMBRE, $COL_APELLIDO, $COL_PASSWORD, $COL_FECHA_NACIMIENTO) VALUES(?, ?, ?, ?);"
    override val UPDATE =
        "UPDATE $DB_TABLE SET $COL_NOMBRE = ?, $COL_APELLIDO = ?, $COL_PASSWORD = ?, $COL_FECHA_NACIMIENTO = ? WHERE $COL_ID_USUARIO = ?;"
    override val DELETE =
        "DELETE FROM $DB_TABLE WHERE $COL_ID_USUARIO = ?;"
    override val SELECT_ONE =
        "SELECT $COL_ID_USUARIO, $COL_NOMBRE, $COL_APELLIDO, $COL_PASSWORD, $COL_FECHA_NACIMIENTO FROM $DB_TABLE WHERE $COL_ID_USUARIO = ?;"

    override fun PreparedStatement.setValues(entidad: Usuario, id: Int){
        setProperties(entidad)
        setId(id, 5)
    }

    override fun PreparedStatement.setProperties(entidad: Usuario) {
        setString(1, entidad.nombre.orEmpty())
        setString(2, entidad.apellido.orEmpty())
        setString(3, entidad.password.orEmpty())
        setString(4, entidad.fechaNacimiento.orEmpty())
    }

    override fun PreparedStatement.setId(id: Int, index: Int) {
        setInt(index, id)
    }

    override fun ResultSet.mapToEntidad(): Usuario =
        Usuario(
            id = getInt(COL_ID_USUARIO),
            nombre = getString(COL_NOMBRE),
            apellido = getString(COL_APELLIDO),
            password = getString(COL_PASSWORD),
            fechaNacimiento = getString(COL_FECHA_NACIMIENTO)
        )
}

