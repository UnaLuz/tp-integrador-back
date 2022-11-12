package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.EncuestaRepository
import ar.edu.unsam.algo3.domain.Encuesta
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EncuestaService {
    @Autowired
    lateinit var EncuestaRepository: EncuestaRepository

    fun deleteEncuestaById(idEncuesta:Int){
    }
    fun editEncuesta(userId : Int, contenidoId : Int){}
    fun updateEncuesta(encuesta: Encuesta){}
    fun create(encuesta: Encuesta){}
    fun getEncuestasPrueba() : List<Encuesta>? {
        val encuestas = EncuestaRepository.selectAll()
        return encuestas
    }
}