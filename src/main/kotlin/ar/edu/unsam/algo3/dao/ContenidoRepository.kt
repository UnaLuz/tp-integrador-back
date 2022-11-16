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
        const val PUNTAJE_MAX = "puntaje_personal"
        const val PUNTAJE_PROM = "puntaje_prom"
        const val TIPO_CONTENIDO = "tipo_contenido"
        const val USUARIO_RESPONDE = "id_usuario_responde"
        const val ID_RESPUESTA = "id_respuesta_usuario"
    }

    // Queries
    val SELECT_INICIO =
        """SELECT c.$ID_CONTENIDO, c.$TITULO, avg(d.velocidad) AS $VELOCIDAD_PROM, avg(re.puntaje) AS $PUNTAJE_PROM, $PUNTAJE_MAX, $TIPO_CONTENIDO, t_mejor_puntaje.$USUARIO_RESPONDE, t_mejor_puntaje.$ID_RESPUESTA
            FROM (descarga d)
            RIGHT JOIN (contenido c)
            ON (d.id_contenido_documento = c.id_contenido OR d.id_contenido_musica = c.id_contenido)
            LEFT JOIN respuesta_encuesta re
            ON re.id_descarga_realizada = d.id_descarga
            LEFT JOIN (
	            SELECT c.$ID_CONTENIDO, re.puntaje AS $PUNTAJE_MAX, re.$USUARIO_RESPONDE, re.id_respuesta_encuesta AS $ID_RESPUESTA
	            FROM (respuesta_encuesta re, descarga d, contenido c)
	            WHERE re.id_descarga_realizada = d.id_descarga
	            AND (d.id_contenido_documento = c.id_contenido OR d.id_contenido_musica = c.id_contenido)
	            AND re.id_usuario_responde = ?
	            GROUP BY c.id_contenido
            ) t_mejor_puntaje
            ON c.id_contenido = t_mejor_puntaje.id_contenido
            WHERE c.tipo_contenido <> 'video'
            GROUP BY c.id_contenido;"""

    val SELECT_REPORTE: String =
        """SELECT c.$TITULO, avg(d.velocidad) AS $VELOCIDAD_PROM, avg(re.puntaje) AS $PUNTAJE_PROM, c.$TIPO_CONTENIDO
           FROM (descarga d)
           RIGHT JOIN (contenido c)
           ON (d.id_contenido_documento = c.id_contenido OR d.id_contenido_musica = c.id_contenido)
           LEFT JOIN respuesta_encuesta re
           ON re.id_descarga_realizada = d.id_descarga
           %s
           GROUP BY c.id_contenido
           ORDER BY %s %s
           LIMIT 5;"""

    val REPORTE_WHERE = "WHERE re.id_usuario_responde = ?"

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
    fun getAllContenidos(idUsuario: Int): List<Contenido>? =
        selectAll(SELECT_INICIO, getPreparedStatement(idUsuario))
        { resultSet -> resultSet.mapToContenidoInicio() }

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

    /**
     * Si hay id se agrega el WHERE, si no lo hay se deja como linea vacía
     */
    private fun getReporteQuery(id: Int?, orderBy: ReporteOrderBy) =
        when (id) {
            null -> SELECT_REPORTE.format("", orderBy.colName, orderQuery(orderBy))
            else -> SELECT_REPORTE.format(REPORTE_WHERE, orderBy.colName, orderQuery(orderBy))
        }

    private fun orderQuery(reporteOrderBy: ReporteOrderBy) =
            when (reporteOrderBy) {
          ReporteOrderBy.TITULO -> "ASC"
                else -> "DESC"
            }
    private fun getPreparedStatement(id: Int?) = id?.let { value ->
        { statement: PreparedStatement ->
            statement.setInt(1, value)
        }
    }

}

fun ResultSet.mapToContenidoInicio(): Contenido {
    val id = getInt(ContenidoRepository.ID_CONTENIDO)
    val titulo = getString(ContenidoRepository.TITULO)
    val velocidadPromedio = getDouble(ContenidoRepository.VELOCIDAD_PROM)
    val puntajeMax = getDouble(ContenidoRepository.PUNTAJE_MAX)
    val puntajePromedio = getDouble(ContenidoRepository.PUNTAJE_PROM)
    val tipoContenido = getString(ContenidoRepository.TIPO_CONTENIDO)
    var idUsuarioResponde: Int? = getInt(ContenidoRepository.USUARIO_RESPONDE)
    idUsuarioResponde = if(this.wasNull()) null else idUsuarioResponde
    var idRespuesta: Int? = getInt(ContenidoRepository.ID_RESPUESTA)
    idRespuesta = if(this.wasNull()) null else idRespuesta
    return Contenido(
        id = id,
        titulo = titulo,
        velocidadPromedio = velocidadPromedio,
        puntajeMax = puntajeMax,
        puntajePromedio = puntajePromedio,
        tipoContenido = tipoContenido,
        idUsuarioResponde = idUsuarioResponde,
        idRespuesta = idRespuesta
    )
}

fun ResultSet.mapToContenidoReporte() = Contenido(
    id = null,
    titulo = getString(ContenidoRepository.TITULO),
    velocidadPromedio = getDouble(ContenidoRepository.VELOCIDAD_PROM),
    puntajeMax = null,
    puntajePromedio = getDouble(ContenidoRepository.PUNTAJE_PROM),
    tipoContenido = getString(ContenidoRepository.TIPO_CONTENIDO),
    idUsuarioResponde = null,
    idRespuesta = null
)