package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.utils.Logger
import java.sql.*

/**
 * Clase encargada de la conexion con la base de datos
 */
object DBConnection {
    private val TAG = DBConnection::class.simpleName.toString()
    private const val DB_HOST = "localhost"
    private const val DB_PORT = "3306"
    private const val DB_NAME = "tpintegrador"
    private const val DB_USER = "root"
    private const val DB_PASS = "root"

    private const val DB_CONNECTION_URL =
        """jdbc:mysql://%s:%s/%s?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true"""

    fun <T> executeQuery(query: String, block: (ResultSet) -> T) =
        useStatement(query) { statement ->
            try {
                statement.executeQuery().use { resultSet ->
                    block(resultSet)
                }
            } catch (ex: SQLTimeoutException) {
                Logger.error(tag = TAG, message = "Error al ejecutar la query", exception = ex)
                null
            } catch (ex: SQLException) {
                Logger.error(tag = TAG, message = "Error al ejecutar la query", exception = ex)
                null
            }
        }

    fun executeUpdate(query: String, setValuesBlock: (PreparedStatement) -> Unit) =
        try {
            useStatement(query = query) { statement ->
                setValuesBlock(statement)
                statement.executeUpdate()
            }
        } catch (ex: SQLTimeoutException) {
            Logger.error(tag = TAG, message = "Error al actualizar registros", exception = ex)
            null
        } catch (ex: SQLException) {
            Logger.error(tag = TAG, message = "Error al actualizar registros", exception = ex)
            null
        }

    private fun getNewConnection(host: String = DB_HOST, port: String = DB_PORT, name: String = DB_NAME): Connection? {
        val url = DB_CONNECTION_URL.format(host, port, name)
        return try {
            DriverManager.getConnection(url, DB_USER, DB_PASS)
        } catch (sqlException: SQLException) {
            Logger.error(tag = TAG, message = "Fallo al iniciar conexion con mysql", exception = sqlException)
            null
        }
    }

    private fun <T> useConnection(block: (Connection) -> T): T? =
        getNewConnection()?.use(block)

    private fun <T> useStatement(query: String, block: (PreparedStatement) -> T): T? =
        useConnection { connection ->
            try {
                connection.prepareStatement(query).use(block)
            } catch (ex: SQLException) {
                Logger.error(tag = TAG, message = "Error al preparar la sentencia", exception = ex)
                null
            }
        }
}
