package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.ReporteOrderBy
import ar.edu.unsam.algo3.dao.ContenidoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ContenidoService {

    @Autowired
    lateinit var ContenidoRepository: ContenidoRepository

    fun getAllContenidos(idUsuario: Int) = ContenidoRepository.getAllContenidos(idUsuario)

    fun getReporteContenidos(
        idUsuario: Int?,
        orderBy: ReporteOrderBy?
    ) = ContenidoRepository.getReporteContenidos(idUsuario, orderBy ?: ReporteOrderBy.PUNTAJE)

}