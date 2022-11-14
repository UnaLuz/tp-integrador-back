package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.DescargaRepository
import ar.edu.unsam.algo3.domain.Descarga
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DescargaService {

    @Autowired
    lateinit var descargaRepository: DescargaRepository

    fun createDescarga(descarga: Descarga): Int = descargaRepository.createDescarga(descarga)

}