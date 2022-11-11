package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.domain.Contenido
import java.sql.PreparedStatement
import java.sql.ResultSet

object ContenidoDAO : EntidadDAO<Contenido> {
    private val TAG = this::class.simpleName.toString()

    // DB info
    override val DB_TABLE = "contenido"
    const val COL_ID_CONTENIDO = "id_contenido"
    const val COL_TITULO = "titulo"
    const val COL_EXTENSION = "extension"
    const val COL_FECHA_PUBLICACION = "fehca_publicacion"
    const val COL_TIPO_CONTENIDO = "tipo_contenido"

    // Queries
    override val SELECT =
        "SELECT $COL_ID_CONTENIDO, $COL_TITULO, $COL_EXTENSION, $COL_FECHA_PUBLICACION, $COL_TIPO_CONTENIDO FROM $DB_TABLE;"
    override val INSERT =
        "INSERT INTO $DB_TABLE($COL_TITULO, $COL_EXTENSION, $COL_FECHA_PUBLICACION, $COL_TIPO_CONTENIDO) VALUES(?, ?, ?, ?);"
    override val UPDATE =
        "UPDATE $DB_TABLE SET $COL_TITULO = ?, $COL_EXTENSION = ?, $COL_FECHA_PUBLICACION = ?, $COL_TIPO_CONTENIDO = ? WHERE $COL_ID_CONTENIDO = ?;"
    override val DELETE =
        "DELETE FROM $DB_TABLE WHERE $COL_ID_CONTENIDO = ?;"
    override val SELECT_ONE =
        "SELECT $COL_ID_CONTENIDO, $COL_TITULO, $COL_EXTENSION, $COL_FECHA_PUBLICACION, $COL_TIPO_CONTENIDO FROM $DB_TABLE WHERE $COL_ID_CONTENIDO = ?;"

    override fun PreparedStatement.setId(id: Int, index: Int) {
        setInt(index, id)
    }

    override fun PreparedStatement.setValues(entidad: Contenido, id: Int) {
        setProperties(entidad)
        setId(id)
    }

    override fun PreparedStatement.setProperties(entidad: Contenido) {
        setString(1, entidad.titulo.orEmpty())
        setString(2, entidad.extension.orEmpty())
        setString(3, entidad.fechaPublicacion.orEmpty())
        setString(4, entidad.tipoContenido.orEmpty())
    }

    override fun ResultSet.mapToEntidad() =
        Contenido(
            id = getInt(COL_ID_CONTENIDO),
            titulo = getString(COL_TITULO),
            extension = getString(COL_EXTENSION),
            fechaPublicacion = getString(COL_FECHA_PUBLICACION),
            tipoContenido = getString(COL_TIPO_CONTENIDO)
        )

}
