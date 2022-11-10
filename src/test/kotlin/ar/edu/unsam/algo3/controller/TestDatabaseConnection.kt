package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.utils.Logger
import java.sql.DriverManager
import java.sql.SQLException

const val TAG = "TestDatabaseConnection"

fun main(args: Array<String>) {
    val url =
        "jdbc:mysql://localhost:3306/tpintegrador?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true"

    try {
        val conexion = DriverManager.getConnection(url, "root", "root")

        val sentencia = conexion.createStatement()
        val sql = "SELECT id_usuario, nombre, apellido FROM usuario"
        val resultSet = sentencia.executeQuery(sql)

        while (resultSet.next()) {
            Logger.debug(TAG, "id_usuario: ${resultSet.getInt("id_usuario")}")
        }
    } catch (sqlException: SQLException) {
        Logger.error(tag = TAG, message = "Fallo al iniciar conexion con mysql", exception = sqlException)
    }
}