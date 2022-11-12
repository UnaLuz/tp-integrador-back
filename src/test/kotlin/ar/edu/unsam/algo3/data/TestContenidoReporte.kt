package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.utils.Logger

fun main() {
    Logger.info(TAG, "--- Select Contenido de reporte ---")

    ContenidoReporteDAO.selectAll()?.forEach { contenido ->
        Logger.info(TAG, contenido.toString())
    } ?: Logger.warning(TAG, "La lista de contenidos no pudo ser recuperada")

    Logger.info(TAG, "--- Select Contenido de reporte de Usuario=1---")

    ContenidoReporteDAO.selectAll(1)?.forEach { contenido ->
        Logger.info(TAG, contenido.toString())
    } ?: Logger.warning(TAG, "La lista de contenidos no pudo ser recuperada")

}