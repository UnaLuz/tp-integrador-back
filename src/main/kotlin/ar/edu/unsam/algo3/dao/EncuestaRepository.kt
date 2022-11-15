package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.domain.Encuesta
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class EncuestaRepository() : EntidadRepository<Encuesta> {
    companion object {
        // Table info
        const val DB_TABLE = "respuesta_encuesta"
        const val COL_ID_Encuesta = "id_respuesta_encuesta"
        const val COL_RESUMENPOS = "resumen_positivo"
        const val COL_RESUMENNEG = "resumen_negativo"
        const val COL_PUNTAJE = "puntaje"
        const val COL_DESCARGA = "id_descarga_realizada"
        const val COL_USUARIO = "id_usuario_responde"

        // Errores
        const val ERROR = -2
    }

    // Queries
    private val SELECT =
        """SELECT $COL_ID_Encuesta, $COL_RESUMENPOS, $COL_RESUMENNEG, $COL_PUNTAJE
        FROM $DB_TABLE;"""

    private val SELECT_BY_ID =
        """SELECT $COL_ID_Encuesta, $COL_RESUMENPOS, $COL_RESUMENNEG, $COL_PUNTAJE
        FROM $DB_TABLE
        WHERE $DB_TABLE.$COL_ID_Encuesta = ?;"""

    private val SELECT_ONE = """
        SELECT re.$COL_ID_Encuesta, re.$COL_RESUMENPOS, re.$COL_RESUMENNEG, re.$COL_PUNTAJE
        FROM contenido c
        INNER JOIN descarga d
        ON (d.id_contenido_documento = ? OR d.id_contenido_musica = ?)
        INNER JOIN $DB_TABLE re
        ON re.$COL_DESCARGA = d.id_descarga
        WHERE re.$COL_USUARIO = ? AND c.id_contenido = ?
        ORDER BY re.$COL_ID_Encuesta DESC
        LIMIT 1;"""

    private val INSERT =
        """INSERT INTO $DB_TABLE ($COL_RESUMENPOS, $COL_RESUMENNEG, $COL_PUNTAJE, $COL_DESCARGA, $COL_USUARIO)
        VALUES (?, ?, ?, ?, ?);"""

    private val UPDATE =
        """UPDATE $DB_TABLE
        SET $COL_RESUMENPOS = ?, $COL_RESUMENNEG = ?, $COL_PUNTAJE = ?
        WHERE $COL_ID_Encuesta = ?;"""

    private val DELETE = "DELETE FROM $DB_TABLE WHERE $COL_ID_Encuesta = ?;"

    fun selectAll(): List<Encuesta>? =
        selectAll(SELECT) { it.mapToEncuesta() }

    /**
     * Dado un [idUsuario] y un [idContenido]
     *  busca la última respuesta de encuesta
     *  de ese usuario sobre la descarga de ese contenido
     *
     *  @return [Encuesta] o [NULL] si ocurrió un error en la busqueda
     */
    fun selectOne(idUsuario: Int, idContenido: Int): Encuesta? =
        selectOne(SELECT_ONE, { stmt ->
            stmt.setInt(1, idContenido)
            stmt.setInt(2, idContenido)
            stmt.setInt(3, idUsuario)
            stmt.setInt(4, idContenido)
        }) { it.mapToEncuesta() }

    fun getEncuestaById(idEncuesta: Int): Encuesta? =
        selectOne(SELECT_BY_ID, { stmt ->
            stmt.setInt(1, idEncuesta)
        }) { it.mapToEncuesta() }

    /**
     * Inserta una nueva encuesta (respuesta)
     *
     * Para hacerlo, [idDescarga] e [idUsuario] no pueden ser null
     *
     * Si alguno es null, devuelve [ERROR]
     */
    fun insert(encuesta: Encuesta): Int =
        encuesta.idDescarga?.let { descarga ->
            encuesta.idUsuario?.let { usuario ->
                executeUpdate(INSERT) { stmt ->
                    stmt.setString(1, encuesta.resumenPositivo)
                    stmt.setString(2, encuesta.resumenNegativo)
                    stmt.setDouble(3, encuesta.puntaje)
                    stmt.setInt(4, descarga)
                    stmt.setInt(5, usuario)
                }
            } ?: ERROR
        } ?: ERROR


    fun update(encuesta: Encuesta) =
        encuesta.id?.let { id ->
            executeUpdate(UPDATE) {
                it.setString(1, encuesta.resumenPositivo)
                it.setString(2, encuesta.resumenNegativo)
                it.setDouble(3, encuesta.puntaje)
                it.setInt(4, id)
            }
        } ?: ERROR

    fun deleteEncuestaById(idEncuesta: Int): Int =
        executeUpdate(DELETE) {
            it.setInt(1, idEncuesta)
        }

}

fun ResultSet.mapToEncuesta(): Encuesta =
    Encuesta(
        id = getInt(EncuestaRepository.COL_ID_Encuesta),
        resumenPositivo = getString(EncuestaRepository.COL_RESUMENPOS),
        resumenNegativo = getString(EncuestaRepository.COL_RESUMENNEG),
        puntaje = getDouble(EncuestaRepository.COL_PUNTAJE),
        idDescarga = null,
        idUsuario = null
    )