package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.ReporteOrderBy
import ar.edu.unsam.algo3.domain.Contenido
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.ResultSet

@Repository
class ContenidoRepository() : EntidadRepository<Contenido> {

    companion object {
        // DB info
        const val DB_TABLE = "contenido"
        const val ID_CONTENIDO = "id_contenido"
        const val TITULO = "titulo"
        const val VELOCIDAD_PROM = "velocidad_prom"
        const val PUNTAJE_MAX = "puntaje_max"
        const val PUNTAJE_PROM = "puntaje_prom"
        const val TIPO_CONTENIDO = "tipo_contenido"
    }

    // Queries
    val SELECT_INICIO =
        """SELECT c.$ID_CONTENIDO, c.$TITULO, avg(d.velocidad) AS $VELOCIDAD_PROM, max(re.puntaje) AS $PUNTAJE_MAX, avg(re.puntaje) AS $PUNTAJE_PROM, c.$TIPO_CONTENIDO AS $TIPO_CONTENIDO
        FROM ($DB_TABLE c)
        LEFT JOIN (descarga d)
        ON (d.id_contenido_documento = c.$ID_CONTENIDO OR d.id_contenido_musica = c.$ID_CONTENIDO)
        LEFT JOIN respuesta_encuesta re
        ON re.id_descarga_realizada = d.id_descarga
        WHERE c.tipo_contenido LIKE 'musica' OR c.tipo_contenido LIKE 'documento' 
        GROUP BY c.$ID_CONTENIDO;"""

    val SELECT_REPORTE: String =
        """SELECT c.$TITULO, avg(d.velocidad) AS $VELOCIDAD_PROM, avg(re.puntaje) AS $PUNTAJE_PROM
        FROM (contenido c)
        LEFT JOIN (descarga d)
        ON (d.id_contenido_documento = c.id_contenido OR d.id_contenido_musica = c.id_contenido)
        INNER JOIN respuesta_encuesta re
        ON re.id_descarga_realizada = d.id_descarga
        GROUP BY c.id_contenido
        ORDER BY %s DESC
        LIMIT 5;"""

    val SELECT_REPORTE_WHERE: String =
        """SELECT c.$TITULO, avg(d.velocidad) AS $VELOCIDAD_PROM, avg(re.puntaje) AS $PUNTAJE_PROM
        FROM (contenido c)
        LEFT JOIN (descarga d)
        ON (d.id_contenido_documento = c.id_contenido OR d.id_contenido_musica = c.id_contenido)
        INNER JOIN respuesta_encuesta re
        ON re.id_descarga_realizada = d.id_descarga
        WHERE re.id_usuario_responde = ?
        GROUP BY c.id_contenido
        ORDER BY %s DESC
        LIMIT 5;"""

    /**
     * Busca la lista de contenidos con los datos necesarios
     *  para el inicio:
     *  ```
     *  [
     *   {
     *   "id": 1,
     *   "titulo": "Mcfly Ca7riel",
     *   "velocidadPromedio": 4.0,
     *   "puntajeMax": 5.0,
     *   "puntajePromedio": 3.0
     *   }
     *  ]
     *  ```
     *
     * @return Una lista de contenidos o NULL si ocurrió un error
     */
    fun getAllContenidos(): List<Contenido>? =
        selectAll(SELECT_INICIO) { resultSet -> resultSet.mapToContenidoInicio() }

    /**
     * Devuelve la lista para el reporte
     *
     * Si [idUsuario] no es nulo, entonces se obtienen los contenidos de el usuario con ese id
     */
    fun getReporteContenidos(idUsuario: Int?, orderBy: ReporteOrderBy): List<Contenido>? =
        selectAll(
            query = getReporteQuery(idUsuario, orderBy),
            prepareStatement = getPreparedStatement(idUsuario)
        ) { resultSet -> resultSet.mapToContenidoReporte() }

    private fun getReporteQuery(id: Int?, orderBy: ReporteOrderBy) =
        when (id) {
            null -> SELECT_REPORTE
            else -> SELECT_REPORTE_WHERE
        }.format(orderBy.colName)

    private fun getPreparedStatement(id: Int?) = id?.let { value ->
        { statement: PreparedStatement ->
            statement.setInt(1, value)
        }
    }

}

fun ResultSet.mapToContenidoInicio() = Contenido(
    id = getInt(ContenidoRepository.ID_CONTENIDO),
    titulo = getString(ContenidoRepository.TITULO),
    velocidadPromedio = getDouble(ContenidoRepository.VELOCIDAD_PROM),
    puntajeMax = getDouble(ContenidoRepository.PUNTAJE_MAX),
    puntajePromedio = getDouble(ContenidoRepository.PUNTAJE_PROM),
    tipoContenido = getString(ContenidoRepository.TIPO_CONTENIDO)
)

fun ResultSet.mapToContenidoReporte() = Contenido(
    id = null,
    titulo = getString(ContenidoRepository.TITULO),
    velocidadPromedio = getDouble(ContenidoRepository.VELOCIDAD_PROM),
    puntajeMax = null,
    puntajePromedio = getDouble(ContenidoRepository.PUNTAJE_PROM),
    tipoContenido = getString(ContenidoRepository.TIPO_CONTENIDO)
)