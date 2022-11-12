package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.ContenidoReporte
import java.sql.PreparedStatement
import java.sql.ResultSet

object ContenidoReporteDAO : EntidadDAO<ContenidoReporte> {

    // Errores
    const val DB_ERROR = -1

    // Table info
    override val DB_TABLE = "contenido"
    const val TITULO = "titulo"
    const val VELOCIDAD_PROM = "velocidad_prom"
    const val PUNTAJE_PROM = "puntaje_prom"

    // Queries
    override val SELECT =
        "SELECT c.$TITULO, avg(d.velocidad) AS $VELOCIDAD_PROM, avg(re.puntaje) AS $PUNTAJE_PROM\n" +
                "FROM (contenido c)\n" +
                "LEFT JOIN (descarga d)\n" +
                "ON (d.id_contenido_documento = c.id_contenido OR d.id_contenido_musica = c.id_contenido)\n" +
                "INNER JOIN respuesta_encuesta re\n" +
                "ON re.id_descarga_realizada = d.id_descarga\n" +
                "-- WHERE re.id_usuario_responde = 1\n" +
                "GROUP BY c.id_contenido\n" +
                "ORDER BY $PUNTAJE_PROM DESC\n" +
                "LIMIT 5;"

    override val INSERT: String = ""
    override val UPDATE: String = ""
    override val DELETE: String = ""
    override val SELECT_ONE: String = ""

    override fun insert(entidad: ContenidoReporte): Int = DB_ERROR
    override fun update(entidad: ContenidoReporte): Int = DB_ERROR
    override fun delete(entidad: ContenidoReporte): Int = DB_ERROR
    override fun selectOne(entidad: ContenidoReporte): ContenidoReporte? = null

    override fun PreparedStatement.setId(entidad: ContenidoReporte, index: Int) {
        // No se usa
    }

    override fun PreparedStatement.setValues(entidad: ContenidoReporte) {
        setProperties(entidad)
        setId(entidad)
    }

    override fun PreparedStatement.setProperties(entidad: ContenidoReporte) {
        // Nada porque no se insertan contenidos de inicio, solo se consultan
    }

    override fun ResultSet.mapToEntidad() =
        ContenidoReporte(
            titulo = getString(TITULO),
            velocidadPromedio = getDouble(VELOCIDAD_PROM),
            puntajePromedio = getDouble(PUNTAJE_PROM)
        )

}
