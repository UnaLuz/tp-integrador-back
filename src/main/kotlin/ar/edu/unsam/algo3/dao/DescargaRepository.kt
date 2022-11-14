package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.domain.Descarga
import org.springframework.stereotype.Repository

@Repository
class DescargaRepository : EntidadRepository<Descarga> {

    companion object {
        const val ERROR = -2

        const val DB_TABLE = "descarga"
        const val COL_VELOCIDAD = "velocidad"
        const val COL_ID_USUARIO = "id_usuario_realiza"
        const val COL_ID_MUSICA = "id_contenido_musica"
        const val COL_ID_DOC = "id_contenido_documento"
    }

    val INSERT_TRANSACTION = """
        START TRANSACTION;
          INSERT INTO $DB_TABLE ($COL_VELOCIDAD, $COL_ID_USUARIO, %s)
          VALUES (?, ?, ?);
          SELECT LAST_INSERT_ID();
        COMMIT;
    """.trimIndent()

    /**
     * Inserta una nueva descarga en la base de datos
     *
     * @return el valor del id del objeto insertado
     */
    fun createDescarga(descarga: Descarga): Int =
        descarga.idContenidoMusica?.let {
            createDescargaWithContenido(COL_ID_MUSICA, descarga, it)
        } ?: descarga.idContenidoDocumento?.let {
            createDescargaWithContenido(COL_ID_DOC, descarga, it)
        } ?: ERROR

    private fun createDescargaWithContenido(column: String, descarga: Descarga, idMusica: Int): Int =
        executeUpdate(INSERT_TRANSACTION.format(column)) { statement ->
            statement.setDouble(1, descarga.velocidad)
            statement.setInt(2, descarga.idUsuario)
            statement.setInt(3, idMusica)
        }
}
