package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.domain.Descarga
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class DescargaRepository : EntidadRepository<Descarga> {

    companion object {
        const val ERROR = -2

        const val DB_TABLE = "descarga"
        const val COL_ID_DESCARGA = "id_descarga"
        const val COL_VELOCIDAD = "velocidad"
        const val COL_ID_USUARIO = "id_usuario_realiza"
        const val COL_ID_MUSICA = "id_contenido_musica"
        const val COL_ID_DOC = "id_contenido_documento"
    }

    val INSERT = """INSERT INTO $DB_TABLE ($COL_VELOCIDAD, $COL_ID_USUARIO, %s)
          VALUES (?, ?, ?);"""

    val SELECT = "SELECT * FROM $DB_TABLE WHERE $COL_ID_DESCARGA = LAST_INSERT_ID();"

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

    private fun createDescargaWithContenido(column: String, descarga: Descarga, idContenido: Int): Int? {
        val descargaInsert = executeTransaction(INSERT.format(column), SELECT, { statement ->
            statement.setDouble(1, descarga.velocidad)
            statement.setInt(2, descarga.idUsuario)
            statement.setInt(3, idContenido)
        }) {
            it.mapToDescarga()
        }
        return descargaInsert?.id
    }
}

fun ResultSet.mapToDescarga(): Descarga {
    var id: Int? = getInt(DescargaRepository.COL_ID_DESCARGA)
    if(this.wasNull()) id = null
    val velocidad = getDouble(DescargaRepository.COL_VELOCIDAD)
    val idUsuario = getInt(DescargaRepository.COL_ID_USUARIO)
    var idContenidoMusica: Int? = getInt(DescargaRepository.COL_ID_MUSICA)
    if(this.wasNull()) idContenidoMusica = null
    var idContenidoDocumento: Int? = getInt(DescargaRepository.COL_ID_DOC)
    if(this.wasNull()) idContenidoDocumento = null
    return Descarga(
        id = id,
        velocidad = velocidad,
        idUsuario = idUsuario,
        idContenidoMusica = idContenidoMusica,
        idContenidoDocumento = idContenidoDocumento
    )
}