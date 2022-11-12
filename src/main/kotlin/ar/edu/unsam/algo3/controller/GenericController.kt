package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Encuesta
import ar.edu.unsam.algo3.service.ContenidoService
import ar.edu.unsam.algo3.service.EncuestaService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class GenericController {

    @Autowired
    lateinit var ContenidoService: ContenidoService
    @Autowired
    lateinit var EncuestaService: EncuestaService
    /*@GetMapping("")
    @Operation(summary = "Devuelve todos los contenidos para la página de inicio")
    fun getAllContenidos() = ContenidoService.getAllContenidos()

    @GetMapping("/getReporte")
    @Operation(summary = "Devuelve todos los contenidos para el reporte")
    fun getReporteContenidos() = ContenidoService.getReporteContenidos()
*/
    @GetMapping("/getEncuestasPrueba")
    @Operation(summary = "Devuelve todos los contenidos para el reporte")
    fun getEncuestasPrueba() = EncuestaService.getEncuestasPrueba()
    @PostMapping("/createEncuesta")
    @Operation(summary = "Crea una nuevo Encuesta")
    fun createEncuesta(@RequestBody EncuestaBody : Encuesta) {
        return EncuestaService.create(EncuestaBody)
    }

    @PutMapping("/encuesta")
    @Operation(summary = "Actualiza una encuesta")
    fun updateEncuestaById(@RequestBody EncuestaBody: Encuesta) = EncuestaService.updateEncuesta(EncuestaBody)

    @DeleteMapping("/deleteEncuesta/{id}")
    @Operation(summary = "Eliminar una encuesta")
    fun deleteEncuestaById(@PathVariable id:Int) = EncuestaService.deleteEncuestaById(id)

    @GetMapping("/editEncuesta/{userId}/contenido/{contenidoId}")
    fun editEncuesta(@PathVariable userId:Int, @PathVariable contenidoId:Int) = EncuestaService.editEncuesta(userId,contenidoId)

  /*  @PostMapping("/createDescarga")
    @Operation(summary = "generar descarga de contenido")
    fun createDescarga() = ContenidoService.createDescarga()
*/

   /* @RequestMapping("/editEncuesta", params = ["userId","contenidoId"])
    //@GetMapping("/usuario/{user}")
    @Operation(summary = "Devuelve un usuario que coincida user y pass")
    fun editEncuesta(@RequestParam user:String, pass:Int) = UsuarioService.getUsuarioLogin(user,pass)*/
}