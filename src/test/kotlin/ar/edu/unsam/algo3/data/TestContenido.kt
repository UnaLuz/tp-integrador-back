package ar.edu.unsam.algo3.data

import ar.edu.unsam.algo3.utils.Logger

fun main() {

    val contenidoDAO = ContenidoDAO()
    val id = 1

    Logger.info(TAG, "--- Select Contenido de inicio ---")

    contenidoDAO.selectInicio()?.forEach { contenido ->
        Logger.info(TAG, contenido.toString())
    } ?: Logger.warning(TAG, "La lista de contenidos no pudo ser recuperada")

    Logger.info(TAG, "--- Select Contenido de reporte ---")

    contenidoDAO.selectReporte()?.forEach { contenido ->
        Logger.info(TAG, contenido.toString())
    } ?: Logger.warning(TAG, "La lista de contenidos no pudo ser recuperada")

    Logger.info(TAG, "--- Select Contenido de reporte para usuario=$id ---")

    contenidoDAO.selectReporte(id)?.forEach { contenido ->
        Logger.info(TAG, contenido.toString())
    } ?: Logger.warning(TAG, "La lista de contenidos no pudo ser recuperada")
}