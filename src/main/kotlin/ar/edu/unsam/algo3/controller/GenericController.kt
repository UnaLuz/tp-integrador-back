package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.ReporteOrderBy
import ar.edu.unsam.algo3.domain.Descarga
import ar.edu.unsam.algo3.domain.Encuesta
import ar.edu.unsam.algo3.service.ContenidoService
import ar.edu.unsam.algo3.service.DescargaService
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

    @Autowired
    lateinit var descargaService: DescargaService

    // Contenido endpoints

    @GetMapping("")
    @Operation(summary = "Devuelve todos los contenidos para la página de inicio")
    fun getAllContenidos() = ContenidoService.getAllContenidos()

    @GetMapping("/getReporte")
    @Operation(summary = "Devuelve todos los contenidos para el reporte")
    fun getReporteContenidos(
        @RequestParam idUsuario: Int?,
        @RequestParam orderBy: ReporteOrderBy?
    ) = ContenidoService.getReporteContenidos(idUsuario, orderBy)

    // Encuesta endpoints

    //FIXME: Esto era solo una prueba, no utilizar
    @GetMapping("/getEncuestasPrueba")
    @Operation(summary = "Devuelve todos los contenidos para el reporte")
    fun getEncuestasPrueba() = EncuestaService.getEncuestasPrueba()

    @GetMapping("/getEncuesta")
    @Operation(summary = "Devuelve la encuesta más reciente asociada a un usuario y un contenido segun sus IDs")
    fun getEncuestaByUsuarioAndContenido(
        @RequestParam idUsuario: Int,
        @RequestParam idContenido: Int
    ) = EncuestaService.getEncuestaByUsuarioAndContenido(idUsuario, idContenido)

    @PostMapping("/createEncuesta")
    @Operation(summary = "Crea una nuevo Encuesta")
    fun createEncuesta(@RequestBody EncuestaBody: Encuesta): Int {
        return EncuestaService.create(EncuestaBody)
    }

    @PutMapping("/encuesta")
    @Operation(summary = "Actualiza una encuesta")
    fun updateEncuestaById(@RequestBody EncuestaBody: Encuesta) =
        EncuestaService.updateEncuesta(EncuestaBody)

    @DeleteMapping("/deleteEncuesta/{id}")
    @Operation(summary = "Eliminar una encuesta")
    fun deleteEncuestaById(@PathVariable id: Int) = EncuestaService.deleteEncuestaById(id)

    @PostMapping("/createDescarga")
    @Operation(summary = "generar descarga de contenido")
    fun createDescarga(@RequestBody descarga: Descarga): Int = descargaService.createDescarga(descarga)

    /* @RequestMapping("/editEncuesta", params = ["userId","contenidoId"])
     //@GetMapping("/usuario/{user}")
     @Operation(summary = "Devuelve un usuario que coincida user y pass")
     fun editEncuesta(@RequestParam user:String, pass:Int) = UsuarioService.getUsuarioLogin(user,pass)*/
}