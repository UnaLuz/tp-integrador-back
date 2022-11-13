import ar.edu.unsam.algo3.dao.EntidadRepository
import ar.edu.unsam.algo3.domain.Contenido
import org.springframework.stereotype.Repository
import java.sql.ResultSet
@Repository
class ContenidoRepository () : EntidadRepository<Contenido> {

    companion object {
        // DB info
        const val DB_TABLE = "contenido"
        const val ID_CONTENIDO = "id_contenido"
        const val TITULO = "titulo"
        const val VELOCIDAD_PROM = "velocidad_prom"
        const val PUNTAJE_MAX = "puntaje_max"
        const val PUNTAJE_PROM = "puntaje_prom"
    }

    fun getAllContenidos() {}
    fun getReporteContenidos() {}

    fun createDescarga() {}

    // Queries
    val SELECT_INICIO =
        "SELECT c.$ID_CONTENIDO, c.$TITULO, avg(d.velocidad) AS $VELOCIDAD_PROM, max(re.puntaje) AS $PUNTAJE_MAX, avg(re.puntaje) AS $PUNTAJE_PROM\n" +
                "FROM ($DB_TABLE c)\n" +
                "LEFT JOIN (descarga d)\n" +
                "ON (d.id_contenido_documento = c.$ID_CONTENIDO OR d.id_contenido_musica = c.$ID_CONTENIDO)\n" +
                "LEFT JOIN respuesta_encuesta re\n" + "ON re.id_descarga_realizada = d.id_descarga\n" +
                "GROUP BY c.$ID_CONTENIDO;"

    val SELECT_REPORTE: String =
        "SELECT c.${TITULO}, avg(d.velocidad) AS ${VELOCIDAD_PROM}, avg(re.puntaje) AS ${PUNTAJE_PROM}\n" + "FROM (contenido c)\n" + "LEFT JOIN (descarga d)\n" + "ON (d.id_contenido_documento = c.id_contenido OR d.id_contenido_musica = c.id_contenido)\n" + "INNER JOIN respuesta_encuesta re\n" + "ON re.id_descarga_realizada = d.id_descarga\n" + "GROUP BY c.id_contenido\n" + "ORDER BY ${PUNTAJE_PROM} DESC\n" + "LIMIT 5;"

    val SELECT_REPORTE_WHERE: String =
        "SELECT c.${TITULO}, avg(d.velocidad) AS ${VELOCIDAD_PROM}, avg(re.puntaje) AS ${PUNTAJE_PROM}\n" + "FROM (contenido c)\n" + "LEFT JOIN (descarga d)\n" + "ON (d.id_contenido_documento = c.id_contenido OR d.id_contenido_musica = c.id_contenido)\n" + "INNER JOIN respuesta_encuesta re\n" + "ON re.id_descarga_realizada = d.id_descarga\n" + "WHERE re.id_usuario_responde = ?\n" + "GROUP BY c.id_contenido\n" + "ORDER BY ${PUNTAJE_PROM} DESC\n" + "LIMIT 5;"

    fun selectInicio(): List<Contenido>? =
        selectAll(SELECT_INICIO) { resultSet -> resultSet.mapToContenidoInicio() }

    /**
     * Devuelve la lista para el reporte
     *
     * Si [id] no es nulo, entonces se obtienen los contenidos de el usuario con ese id
     */
    fun selectReporte(id: Int? = null): List<Contenido>? = id?.let { idNotNull ->
        selectAll(SELECT_REPORTE_WHERE, { statement ->
            statement.setInt(1, idNotNull)
        }) { resultSet -> resultSet.mapToContenidoReporte() }
    } ?: selectAll(SELECT_REPORTE) { resultSet -> resultSet.mapToContenidoReporte() }

}

fun ResultSet.mapToContenidoInicio() = Contenido(
    id = getInt(ContenidoRepository.ID_CONTENIDO),
    titulo = getString(ContenidoRepository.TITULO),
    velocidadPromedio = getDouble(ContenidoRepository.VELOCIDAD_PROM),
    puntajeMax = getDouble(ContenidoRepository.PUNTAJE_MAX),
    puntajePromedio = getDouble(ContenidoRepository.PUNTAJE_PROM)
)

fun ResultSet.mapToContenidoReporte() = Contenido(
    id = null,
    titulo = getString(ContenidoRepository.TITULO),
    velocidadPromedio = getDouble(ContenidoRepository.VELOCIDAD_PROM),
    puntajeMax = null,
    puntajePromedio = getDouble(ContenidoRepository.PUNTAJE_PROM)
)