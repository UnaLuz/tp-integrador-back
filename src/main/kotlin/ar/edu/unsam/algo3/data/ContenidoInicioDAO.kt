package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.ContenidoInicio
import java.sql.PreparedStatement
import java.sql.ResultSet

object ContenidoInicioDAO : EntidadDAO<ContenidoInicio> {

    // Errores
    const val DB_ERROR = -1

    // DB info
    override val DB_TABLE = "contenido"
    const val ID_CONTENIDO = "id_contenido"
    const val TITULO = "titulo"
    const val VELOCIDAD_PROM = "velocidad_prom"
    const val PUNTAJE_MAX = "puntaje_max"
    const val PUNTAJE_PROM = "puntaje_prom"

    // Queries
    override val SELECT =
        "SELECT c.$ID_CONTENIDO, c.$TITULO, avg(d.velocidad) AS $VELOCIDAD_PROM, max(re.puntaje) AS $PUNTAJE_MAX, avg(re.puntaje) AS $PUNTAJE_PROM\n" +
                "FROM (contenido c)\n" +
                "LEFT JOIN (descarga d)\n" +
                "ON (d.id_contenido_documento = c.$ID_CONTENIDO OR d.id_contenido_musica = c.$ID_CONTENIDO)\n" +
                "LEFT JOIN respuesta_encuesta re\n" +
                "ON re.id_descarga_realizada = d.id_descarga\n" +
                "GROUP BY c.$ID_CONTENIDO;"

    override val INSERT: String = ""
    override val UPDATE: String = ""
    override val DELETE: String = ""
    override val SELECT_ONE: String = ""

    override fun insert(entidad: ContenidoInicio): Int = DB_ERROR
    override fun update(entidad: ContenidoInicio): Int = DB_ERROR
    override fun delete(entidad: ContenidoInicio): Int = DB_ERROR

    override fun PreparedStatement.setId(entidad: ContenidoInicio, index: Int) {
        setInt(index, entidad.id)
    }

    override fun PreparedStatement.setValues(entidad: ContenidoInicio) {
        setProperties(entidad)
        setId(entidad)
    }

    override fun PreparedStatement.setProperties(entidad: ContenidoInicio) {
        // Nada porque no se insertan contenidos de inicio, solo se consultan
    }

    override fun ResultSet.mapToEntidad() =
        ContenidoInicio(
            id = getInt(ID_CONTENIDO),
            titulo = getString(TITULO),
            velocidadPromedio = getDouble(VELOCIDAD_PROM),
            puntajeMax = getDouble(PUNTAJE_MAX),
            puntajePromedio = getDouble(PUNTAJE_PROM)
        )

}
