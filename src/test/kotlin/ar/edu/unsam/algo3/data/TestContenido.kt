package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.ReporteOrderBy
import ar.edu.unsam.algo3.dao.ContenidoRepository
import ar.edu.unsam.algo3.utils.Logger

fun main() {

    val TAG = "TestContenido"

    val contenidoDAO = ContenidoRepository()
    val id = 1

    Logger.info(TAG, "--- Select Contenido de inicio ---")

    contenidoDAO.getAllContenidos()?.forEach { contenido ->
        Logger.info(TAG, contenido.toString())
    } ?: Logger.warning(TAG, "La lista de contenidos no pudo ser recuperada")

    Logger.info(TAG, "--- Select Contenido de reporte ---")

    contenidoDAO.getReporteContenidos(null, ReporteOrderBy.PUNTAJE)?.forEach { contenido ->
        Logger.info(TAG, contenido.toString())
    } ?: Logger.warning(TAG, "La lista de contenidos no pudo ser recuperada")

    Logger.info(TAG, "--- Select Contenido de reporte para usuario=$id ---")

    contenidoDAO.getReporteContenidos(id, ReporteOrderBy.PUNTAJE)?.forEach { contenido ->
        Logger.info(TAG, contenido.toString())
    } ?: Logger.warning(TAG, "La lista de contenidos no pudo ser recuperada")
}