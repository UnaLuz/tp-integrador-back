package ar.edu.unsam.algo3.data

import java.sql.PreparedStatement
import java.sql.ResultSet

interface EntidadDAO<R> {
    companion object {
        // Errores
        const val DB_ERROR = -1
    }

    // Table Info
    val DB_TABLE: String

    // Queries
    val SELECT: String
    val INSERT: String
    val UPDATE: String
    val DELETE: String
    val SELECT_ONE: String

    /**
     * Realiza un select de todas las filas de la base de datos.
     *
     * @return Una lista de entidades o null si ocurrió un error.
     */
    fun selectAll(): List<R>? =
        DBConnection.executeQuery(SELECT) { resultSet ->
            resultSet.mapToList {
                it.mapToEntidad()
            }
        }

    fun insert(entidad: R): Int =
        DBConnection.executeUpdate(INSERT) {
            it.setProperties(entidad)
        } ?: DB_ERROR

    fun update(entidad: R): Int =
        DBConnection.executeUpdate(UPDATE) {
            it.setValues(entidad)
        } ?: DB_ERROR

    fun delete(entidad: R): Int =
        DBConnection.executeUpdate(DELETE) {
                it.setId(entidad)
            } ?: DB_ERROR

    /**
     * Obtiene la fila con el id de la entidad.
     *
     * @return Una entidad o null si ocurrió un error.
     */
    fun selectOne(entidad: R): R? =
        DBConnection.executeQuery(SELECT_ONE, {
            it.setId(entidad)
        }) { resultSet ->
            if (resultSet.next()) resultSet.mapToEntidad() else null
        }

    /**
     * Extension para setear todas las properties de la entidad menos el id.
     *
     * Se utiliza en operaciones que requieren casi todos los campos como INSERT.
     *
     * Deben ir en orden.
     */
    fun PreparedStatement.setProperties(entidad: R)

    /**
     * Extension para setear todas las properties de la entidad incluyendo el id.
     *
     * Se utiliza en operaciones que requieren de todos los campos como UPDATE.
     *
     * Deben ir en orden.
     */
    fun PreparedStatement.setValues(entidad: R)

    /**
     * Extension para setear el ID.
     *
     * Se utiliza en operaciones que requieren solo del ID como DELETE y SELECT con WHERE.
     */
    fun PreparedStatement.setId(entidad: R, index: Int = 1)

    fun ResultSet.mapToEntidad(): R
}
