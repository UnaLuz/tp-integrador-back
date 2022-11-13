package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.domain.Encuesta
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class EncuestaRepository() : EntidadRepository<Encuesta>{
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
        "SELECT $COL_ID_Encuesta, $COL_RESUMENPOS, $COL_RESUMENNEG, $COL_PUNTAJE FROM $DB_TABLE;"

    private val SELECT_ONE =
        "SELECT $COL_ID_Encuesta, $COL_RESUMENPOS, $COL_RESUMENNEG, $COL_PUNTAJE FROM $DB_TABLE WHERE $COL_ID_Encuesta = ?;"

    private val INSERT =
        "INSERT INTO $DB_TABLE VALUES (?, ?, ?, ?, ?);"

    fun selectAll(): List<Encuesta>? =
        selectAll(SELECT) { it.mapToEncuesta() }

    fun selectOne(id: Int): Encuesta? =
        selectOne(SELECT_ONE, { stmt ->
            stmt.setInt(1, id)
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