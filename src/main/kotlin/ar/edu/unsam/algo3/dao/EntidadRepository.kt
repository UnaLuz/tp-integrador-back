package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.data.DBConnection
import ar.edu.unsam.algo3.data.mapToList
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

interface EntidadRepository<R> {
    companion object {
        // Errores
        const val DB_ERROR = -1
    }

    /**
     * Realiza un select de todas las filas de la tabla.
     *
     * @return Una lista de [R] o null si ocurrió un error.
     */
    fun selectAll(
        query: String,
        prepareStatement: ((PreparedStatement) -> Unit)? = null,
        mapBlock: (ResultSet) -> R
    ): List<R>? =
        DBConnection.executeQuery(query, prepareStatement) { resultSet ->
            resultSet.mapToList(mapBlock)
        }

    /**
     * Realiza operaciones de modificacion de datos como INSERT, UPDATE o DELETE
     */
    fun executeUpdate(query: String, prepareStatement: (PreparedStatement) -> Unit): Int =
        DBConnection.executeUpdate(query, prepareStatement) ?: DB_ERROR

    /**
     * Obtiene la fila con el id especificado.
     *
     * @return [R] o null si ocurrió un error.
     */
    fun selectOne(
        query: String,
        prepareStatement: ((PreparedStatement) -> Unit)? = null,
        mapBlock: (ResultSet) -> R,
    ): R? =
        DBConnection.executeQuery(query, prepareStatement) { resultSet ->
            if (resultSet.next()) mapBlock(resultSet) else null
        }

    fun executeTransaction(
        updateSql: String,
        querySql: String,
        setValuesBlock: (PreparedStatement) -> Unit,
        mapBlock: (ResultSet) -> R,
    ): R? = DBConnection.executeTransaction(updateSql, querySql, setValuesBlock, mapBlock)

}

data class Operacion<T>(
    val query: String,
    val execute: (PreparedStatement) -> Any,
    val prepareStatement: ((PreparedStatement) -> Unit)?,
    val mapBlock: ((ResultSet) -> T)?
)