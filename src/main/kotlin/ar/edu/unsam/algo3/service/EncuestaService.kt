package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.EncuestaRepository
import ar.edu.unsam.algo3.domain.Encuesta
import ar.edu.unsam.algo3.utils.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EncuestaService {
    @Autowired
    lateinit var EncuestaRepository: EncuestaRepository

    fun deleteEncuestaById(idEncuesta: Int) =
        EncuestaRepository.deleteEncuestaById(idEncuesta)

    fun updateEncuesta(encuesta: Encuesta) = EncuestaRepository.update(encuesta)
    fun create(encuesta: Encuesta): Int {
        val result = EncuestaRepository.insert(encuesta)
        if (result < 1)
            Logger.error(
                tag = "EncuestaService",
                message = "Ocurrio un error al insertar $encuesta"
            )
        // TODO: Mandar otro codigo de respuesta http cuando hay un error
        // Actualmente manda 200 aunque falle
        return result
    }

    fun getEncuestaById(idEncuesta: Int): Encuesta? {
        return EncuestaRepository.getEncuestaById(idEncuesta)
    }

}