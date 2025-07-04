package com.example.NoLimits.Multimedia.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.model.VideojuegoModel;
import com.example.NoLimits.Multimedia.service.VideojuegoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// Define la clase como Controlador.
@RestController
@RequestMapping("api/v1/videojuegos")
@Validated
@Tag(name = "Videojuego-Controller.", description = "Operaciones relacionadas con los videojuegos.")
public class VideojuegoController {

    @Autowired
    private VideojuegoService videojuegoService;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping
    @Operation(summary = "Listar Videojuegos.", description = "Obtiene una lista de todos los videojuegos disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de videojuegos obtenida exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class)))
    @ApiResponse(responseCode = "204", description = "No se encontraron videojuegos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class)))
    public ResponseEntity<List<VideojuegoModel>> listarVideojuegos(){
        List<VideojuegoModel> videojuegos = videojuegoService.obtenerVideojuegos();

        if(videojuegos.isEmpty()){                                                                                          // GET ALL.
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videojuegos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/nombre/{nombre}")
    @Operation(
        summary = "Buscar videojuegos por nombre.",
        description = "Obtiene una lista de videojuegos cuyo nombre coincida con el valor proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Videojuegos encontrados exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = VideojuegoModel.class))
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No se encontraron videojuegos con ese nombre."
        )
    })
    public ResponseEntity<List<VideojuegoModel>> buscarPorNombre(@PathVariable("nombre") String nombreVideojuegos){
        List <VideojuegoModel> videojuegos = videojuegoService.obtenerVideojuegoPorNombre(nombreVideojuegos);
        if (videojuegos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videojuegos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/categoria/{categoria}")
    @Operation(
        summary = "Buscar videojuegos por categoría.",
        description = "Obtiene una lista de videojuegos que pertenezcan a la categoría especificada (por ejemplo: Acción, Aventura, RPG)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Videojuegos encontrados exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = VideojuegoModel.class))
            )
        ),                                                                                                                                  // GET BY CATEGORÍA.
        @ApiResponse(
            responseCode = "204",
            description = "No se encontraron videojuegos con esa categoría."
        )
    })
    public ResponseEntity<List<VideojuegoModel>> buscarPorCategoria(@PathVariable("categoria") String categoriaVideojuegos){
        List <VideojuegoModel> videojuegos = videojuegoService.obtenerVideojuegoPorCategoria(categoriaVideojuegos);
        if (videojuegos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videojuegos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/plataforma/{plataforma}")
    @Operation(
        summary = "Buscar videojuegos por plataforma.",
        description = "Obtiene una lista de videojuegos disponibles en la plataforma especificada."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Videojuegos encontrados exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = VideojuegoModel.class))
            )
        ),                                                                                                                      // GET BY PLATAFORMA.
        @ApiResponse(
            responseCode = "204",
            description = "No se encontraron videojuegos con esa plataforma."
        )
    })
    public ResponseEntity<List<VideojuegoModel>> buscarPorPlataforma(@PathVariable("plataforma") String plataformaVideojuegos){
        List <VideojuegoModel> videojuegos = videojuegoService.obtenerVideojuegoPorPlataforma(plataformaVideojuegos);
        if (videojuegos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videojuegos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/desarrollador/{desarrollador}")
    @Operation(
        summary = "Buscar videojuegos por desarrollador.",
        description = "Obtiene una lista de videojuegos desarrollados por la empresa o estudio especificado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Videojuegos encontrados exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = VideojuegoModel.class))
            )
        ),                                                                                                                          // GET BY DESARROLLADOR.
        @ApiResponse(
            responseCode = "204",
            description = "No se encontraron videojuegos con ese desarrollador."
        )
    })
    public ResponseEntity<List<VideojuegoModel>> buscarPorDesarrollador(@PathVariable("desarrollador") String desarrolladorVideojuegos){
        List <VideojuegoModel> videojuegos = videojuegoService.obtenerVideojuegoPorDesarrollador(desarrolladorVideojuegos);
        if (videojuegos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videojuegos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/precio/{precio}")
    @Operation(
        summary = "Buscar videojuegos por precio.",
        description = "Obtiene una lista de videojuegos cuyo precio coincida con el valor especificado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Videojuegos encontrados exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = VideojuegoModel.class))
            )
        ),                                                                                                                          // GET BY PRECIO.
        @ApiResponse(
            responseCode = "204",
            description = "No se encontraron videojuegos con ese precio."
        )
    })
    public ResponseEntity<List<VideojuegoModel>> buscarPorPrecio(@PathVariable("precio") BigDecimal precioVideojuegos){
        List <VideojuegoModel> videojuegos = videojuegoService.obtenerVideojuegoPorPrecio(precioVideojuegos);
        if (videojuegos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videojuegos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping({"/{id}"})
    @Operation(summary = "Buscar Videojuego por ID.", description = "Obtiene un videojuego específico por su ID.")
    @ApiResponse(responseCode = "200", description = "Videojuego encontrado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class)))
    @ApiResponse(responseCode = "404", description = "Videojuego no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class)))
    public ResponseEntity<VideojuegoModel> buscarPorId(@PathVariable("id") Long idVideojuego) {
        VideojuegoModel videojuegoModel = videojuegoService.obtenerVideojuegoPorId(idVideojuego);                                           // GET BY ID.
        return videojuegoModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(videojuegoModel);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping({"/resumen"})
    @Operation(
        summary = "Resumen de videojuegos.",
        description = "Obtiene un resumen con información clave de los videojuegos, como nombre, categoría, plataforma, y precio."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de videojuegos obtenido exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(
                        type = "object",
                        description = "Mapa con atributos resumidos del videojuego (por ejemplo: ID, nombre, plataforma, categoría, precio)."
                    )
                )
            )                                                                                                                                       // GET BY RESUMEN.
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No hay datos de resumen disponibles."
        )
    })
    // Map: Estructura de datos.
    // Object: Objeto creado qhe almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenVideojuegos(){
        List<Map<String, Object>> resumenVideojuego = videojuegoService.obtenerVideojuegosConNombres();
        if(resumenVideojuego.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenVideojuego);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PostMapping
    @Operation(summary = "Guardar Videojuego.", description = "Guarda un nuevo videojuego en la base de datos.")
    @ApiResponse(responseCode = "201", description = "Videojuego guardado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class)))
    @ApiResponse(responseCode = "400", description = "Error al guardar el videojuego.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class)))
    // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.
    public ResponseEntity<VideojuegoModel> guardarVideojuego(@RequestBody VideojuegoModel videojuegoModel){
        VideojuegoModel nuevoVideojuego = videojuegoService.guardarVideojuego(videojuegoModel);                                     // POST.
        // Se creó exitosamente un videojuego
        return ResponseEntity.status(201).body(nuevoVideojuego);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PutMapping({"/{id}"})
    @Operation(summary = "Actualizar Videojuego.", description = "Actualiza un videojuego existente por su ID.")
    @ApiResponse(responseCode = "200", description = "Videojuego actualizado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class)))
    @ApiResponse(responseCode = "404", description = "Videojuego no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class)))
    // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.
    // PathVariable: Variable que se usa para recibir un valor desde la URL.
    public ResponseEntity<VideojuegoModel> actualizarVideojuego(@PathVariable("id") Long idVideojuego, @RequestBody VideojuegoModel videojuegoModel){
        VideojuegoModel videojuegoActualizado = videojuegoService.actualizarVideojuego(idVideojuego, videojuegoModel);
        if(videojuegoActualizado == null){
            return ResponseEntity.notFound().build();                                                                       // PUT.
        }
        return ResponseEntity.ok(videojuegoActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualiza parcialmente un videojuego por su ID (versión simple sin HATEOAS)
    @PatchMapping("/{id}")
    @Operation(
        summary = "Actualizar videojuego parcialmente.",
        description = "Actualiza parcialmente un videojuego existente por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Videojuego actualizado parcialmente exitosamente.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Videojuego no encontrado.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class))
        )
    })
    public ResponseEntity<VideojuegoModel> patchVideojuego(                                                                             // PATCH.
            @PathVariable Long id, // Recibe el ID del videojuego
            @RequestBody VideojuegoModel detallesParciales // Recibe solo los datos a modificar
    ) {
        try {
            // Llama al servicio para aplicar la actualización parcial
            VideojuegoModel videojuegoActualizado = videojuegoService.actualizarVideojuegoParcial(id, detallesParciales);

            // Devuelve 200 OK con el objeto actualizado
            return ResponseEntity.ok(videojuegoActualizado);
        } catch (RuntimeException e) {
            // Si no se encuentra el videojuego, devuelve 404
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar videojuego.", description = "Elimina un videojuego existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Videojuego eliminado exitosamente.")
    @ApiResponse(responseCode = "404", description = "Videojuego no encontrado.")
    // PathVariable: Variable que se usa para recibir un valor desde la URL.
    public ResponseEntity<Void> eliminarVideojuego(@PathVariable("id") Long idVideojuego){                                              // DELETE.
        videojuegoService.eliminarVideojuegoPorId(idVideojuego);
        return ResponseEntity.noContent().build();
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
