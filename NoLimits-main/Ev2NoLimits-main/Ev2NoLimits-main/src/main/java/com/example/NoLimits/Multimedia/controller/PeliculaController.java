package com.example.NoLimits.Multimedia.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
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

import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.repository.PeliculaRepository;
import com.example.NoLimits.Multimedia.service.PeliculaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// Define la clase como Controlador.
@RestController
@RequestMapping("/api/v1/peliculas")
@Validated
@Tag(name = "Películas-Controller.", description = "Operaciones relacionadas con las películas.")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private PeliculaRepository peliculaRepository;

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtiene el mapeo
    @GetMapping
    // Obtiene el mapeo
    @Operation(summary = "obtener todas las películas.", description = "Obtiene una lista de todas las películas.")
    @ApiResponse(responseCode = "200", description = "Película encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PeliculaModel.class))) // GET
                                                                                                                                                                                         // ALL.
    public ResponseEntity<List<PeliculaModel>> listarPeliculas() {
        List<PeliculaModel> peliculas = peliculaService.obtenerPeliculas();
        if (peliculas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peliculas);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar películas por nombre.", description = "Obtiene una lista de películas que coincidan con el nombre proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Películas encontradas exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PeliculaModel.class)))), // GET
                                                                                                                                                                                                                                       // BY
                                                                                                                                                                                                                                       // NOMBRE.
            @ApiResponse(responseCode = "204", description = "No se encontraron películas con ese nombre.")
    })
    public ResponseEntity<List<PeliculaModel>> buscarPorNombre(@PathVariable("nombre") String nombrePelicula) {
        List<PeliculaModel> peliculas = peliculaService.obtenerPeliculaPorNombre(nombrePelicula);
        if (peliculas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peliculas);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar películas por categoría.", description = "Obtiene una lista de películas que pertenezcan a la categoría especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Películas encontradas exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PeliculaModel.class)))), // GET
                                                                                                                                                                                                                                       // BY
                                                                                                                                                                                                                                       // CATEGORÍA.
            @ApiResponse(responseCode = "204", description = "No se encontraron películas con esa categoría.")
    })
    public ResponseEntity<List<PeliculaModel>> buscarPorCategoria(@PathVariable("categoria") String categoriaPelicula) {
        List<PeliculaModel> peliculas = peliculaService.obtenerPeliculaPorCategoria(categoriaPelicula);
        if (peliculas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peliculas);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/precio/{precio}")
    @Operation(summary = "Buscar películas por precio.", description = "Obtiene una lista de películas cuyo precio coincida con el valor especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Películas encontradas exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PeliculaModel.class)))), // GET
                                                                                                                                                                                                                                       // BY
                                                                                                                                                                                                                                       // PRECIO.
            @ApiResponse(responseCode = "204", description = "No se encontraron películas con ese precio.")
    })
    public ResponseEntity<List<PeliculaModel>> buscarPorPrecio(@PathVariable("precio") BigDecimal precioPelicula) {
        List<PeliculaModel> peliculas = peliculaService.obtenerPeliculaPorPrecio(precioPelicula);
        if (peliculas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peliculas);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Buscar por id
    @GetMapping({ "/{id}" })
    // Busca películas por ID.
    @Operation(summary = "Buscar película por ID.", description = "Obtiene una película específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Película encontrada exitosamente.", content = @Content(mediaType = "application/json", // GET
                                                                                                                                                     // BY
                                                                                                                                                     // ID.
                    schema = @Schema(implementation = PeliculaModel.class))),
            @ApiResponse(responseCode = "404", description = "Película no encontrada.")
    })
    public ResponseEntity<PeliculaModel> buscarPorId(@PathVariable("id") Long idPelicula) {
        PeliculaModel peliculaModel = peliculaService.obtenerPeliculaPorId(idPelicula);
        return peliculaModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(peliculaModel);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/plataforma/{plataforma}")
    @Operation(summary = "Buscar películas por plataforma.", description = "Obtiene una lista de películas que estén disponibles en la plataforma especificada (por ejemplo: Netflix, Disney+).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Películas encontradas exitosamente.", content = @Content(mediaType = "application/json", // GET
                                                                                                                                                       // BY
                                                                                                                                                       // PLATAFORMA.
                    array = @ArraySchema(schema = @Schema(implementation = PeliculaModel.class))))
    })
    public List<PeliculaModel> buscarPorPlataforma(@PathVariable("plataforma") String nombre) {
        return peliculaRepository.findByPlataformaPelicula(nombre);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping({ "/resumen" })
    @Operation(summary = "Resumen de películas.", description = "Obtiene un resumen con información clave de las películas, como nombre, categoría, plataforma, precio, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen de películas obtenido exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "object", description = "Mapa con atributos resumidos de la película (por ejemplo: ID, nombre, categoría, plataforma, precio)."))) // GET
                                                                                                                                                                                                                                                                                                                                               // BY
                                                                                                                                                                                                                                                                                                                                               // RESUMEN.
            ),
            @ApiResponse(responseCode = "204", description = "No hay datos de resumen disponibles.")
    })
    // Map: Estructura de datos.
    // Object: Objeto creado qhe almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenPeliculas() {
        List<Map<String, Object>> resumenPelicula = peliculaService.obtenerPeliculasConNombres();
        if (resumenPelicula.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenPelicula);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PostMapping
    // Crea una nueva película.
    @Operation(summary = "Crear las películas.", description = "Crea una nueva película en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Película creada correctamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PeliculaModel.class))),
            @ApiResponse(responseCode = "400", // POST.
                    description = "Solicitud incorrecta. Los datos de la película no son válidos.")
    })
    // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.
    public ResponseEntity<PeliculaModel> guardarPelicula(@RequestBody PeliculaModel peliculaModel) {
        PeliculaModel nuevaPelicula = peliculaService.guardarPelicula(peliculaModel);
        // Se creó exitosamente una película
        return ResponseEntity.status(201).body(nuevaPelicula);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PutMapping({ "/{id}" })
    // Actualiza completamente una película por su ID.
    @Operation(summary = "Actualizar una película.", description = "Actualiza completamente una película por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Película actualizada correctamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PeliculaModel.class))),
            @ApiResponse(responseCode = "404", description = "Película no encontrada.") // PUT.
    })
    // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.
    // PathVariable: Variable que se usa para recibir un valor desde la URL.
    public ResponseEntity<PeliculaModel> actualizarPelicula(@PathVariable("id") Long idPelicula,
            @RequestBody PeliculaModel peliculaModel) {
        PeliculaModel peliculaActualizada = peliculaService.actualizarPelicula(idPelicula, peliculaModel);
        if (peliculaActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(peliculaActualizada);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PatchMapping({ "/{id}" })
    // Actualiza parcialmente una película por su ID.
    @Operation(summary = "Editar parcialmente una película.", description = "Actualiza parcialmente una película por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Película actualizada correctamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PeliculaModel.class))),
            @ApiResponse(responseCode = "404", // PATCH.
                    description = "Película no encontrada.")
    })
    // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.
    // PathVariable: Variable que se usa para recibir un valor desde la URL.
    public ResponseEntity<PeliculaModel> editarPelicula(@PathVariable("id") Long id,
            @RequestBody PeliculaModel peliculaModel) {
        PeliculaModel peliculaActualizada = peliculaService.actualizarPeliculaParcial(id, peliculaModel);
        return peliculaActualizada == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(peliculaActualizada);
    }

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Eliminar un película existente por su ID (versión HAL + Swagger)
        @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE) // Define el endpoint DELETE con respuesta HAL
        @Operation(
            summary = "Eliminar película.", // Título del endpoint
            description = "Elimina una película existente por su ID." // Explicación del endpoint
        )
        @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", // Código 204 No Content
            description = "Película eliminada exitosamente."
        ),
        @ApiResponse(
            responseCode = "404", // Código 404 Not Found
            description = "Película no encontrada."
        )
    })
        public ResponseEntity<?> deletePelicula(@PathVariable Long id) { // Recibe el ID de la película desde la URL                          // DELETE.
        try {
            peliculaService.eliminarPeliculaPorId(id); // Llama al servicio para eliminar la película
            return ResponseEntity.noContent().build(); // Retorna 204 No Content si se eliminó correctamente
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Si falla (ej: no existe), retorna 404 Not Found
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("{id}/estado")//busca las peliculas por si Id y muestra si su estado es activo o inactivo
        @Operation(summary = "Obtener el estado de una película.", description = "Obtiene el estado de una película por su ID, indicando si está activa o inactiva.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Estado de la película obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                @ApiResponse(responseCode = "404", description = "Película no encontrada.")
        })
        // PathVariable: Variable que se usa para recibir un valor desde la URL.
        public ResponseEntity<Map<String, Object>> obtenerEstadoPelicula(@PathVariable Long id) {
            PeliculaModel pelicula = peliculaService.obtenerPeliculaPorId(id);
            if (pelicula == null) {
                throw new RuntimeException("Pelicula no encontrada" );
            }

            Map<String, Object> estado = Map.of(
                    "id", pelicula.getIdPelicula(),
                    "nombre", pelicula.getNombrePelicula(),
                    "activa", pelicula.isActiva()
            );
            return ResponseEntity.ok(estado);
        }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
