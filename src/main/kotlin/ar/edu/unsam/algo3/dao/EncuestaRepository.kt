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
    }

    // Queries
    private val SELECT =
        "SELECT $COL_ID_Encuesta, $COL_RESUMENPOS, $COL_RESUMENNEG, $COL_PUNTAJE FROM $DB_TABLE;"

    private val SELECT_ONE =
        "SELECT $COL_ID_Encuesta, $COL_RESUMENPOS, $COL_RESUMENNEG, $COL_PUNTAJE FROM $DB_TABLE WHERE $COL_ID_Encuesta = ?;"

    fun selectAll(): List<Encuesta>? =
        selectAll(SELECT) { it.mapToEncuesta() }

    fun selectOne(Encuesta: Encuesta): Encuesta? =
        selectOne(SELECT_ONE, { stmt ->
            Encuesta.id?.let { stmt.setInt(1, it) }
        }) { it.mapToEncuesta() }

}

fun ResultSet.mapToEncuesta(): Encuesta =
    Encuesta(
        id = getInt(EncuestaRepository.COL_ID_Encuesta),
        resumenPositivo = getString(EncuestaRepository.COL_RESUMENPOS),
        resumenNegativo = getString(EncuestaRepository.COL_RESUMENNEG),
        puntaje = getDouble(EncuestaRepository.COL_PUNTAJE),
    )