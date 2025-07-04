package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.PeliculaModelAssembler;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.service.PeliculaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/peliculas",  produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Películas-Controller-V2", description = "Operaciones relacionadas con las películas.")
public class PeliculaControllerV2 {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private PeliculaModelAssembler peliculaAssembler;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger: explica el propósito del endpoint.
    @Operation(
        summary = "Obtener todas las películas (HATEOAS)", // Título breve para Swagger UI.
        description = "Obtiene una lista de todas las películas con enlaces HATEOAS." // Descripción más detallada.
    )

    // Documenta la posible respuesta 200 con la lista de películas.
    @ApiResponse(
        responseCode = "200",
        description = "Películas encontradas exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con enlaces HATEOAS.
            schema = @Schema(implementation = PeliculaModel.class) // Modelo de cada película.
        )
    )

    // Documenta la respuesta 204 si no hay películas disponibles.
    @ApiResponse(
        responseCode = "204",
        description = "No hay películas registradas.",
        content = @Content // No se devuelve cuerpo en este caso.
    )

    // Define que este método responde a GET y devuelve una CollectionModel con HATEOAS.
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PeliculaModel>>> getAllPeliculas() {                                                 // GET ALL.
    // Obtiene la lista de películas y las transforma en EntityModels con enlaces HATEOAS.
    List<EntityModel<PeliculaModel>> peliculas = peliculaService.obtenerPeliculas().stream()
            .map(peliculaAssembler::toModel) // Aplica el assembler para agregar los enlaces.
            .collect(Collectors.toList());

    // Si la lista está vacía, devuelve HTTP 204 No Content.
    if (peliculas.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    // Devuelve HTTP 200 OK con una CollectionModel que incluye enlace self.
    return ResponseEntity.ok(
        CollectionModel.of(
            peliculas, // Lista de EntityModel<PeliculaModel>.
            linkTo(methodOn(PeliculaControllerV2.class).getAllPeliculas()).withSelfRel() // Enlace a sí mismo.
        )
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para explicar el objetivo del endpoint.
    @Operation(
        summary = "Buscar película por ID (HATEOAS)", // Título que aparece en Swagger UI.
        description = "Obtiene una película específica por su ID con enlaces HATEOAS." // Descripción más detallada.
    )

    // Define las posibles respuestas para este endpoint.
    @ApiResponses(value = {
    // Cuando se encuentra la película.
    @ApiResponse(
        responseCode = "200",
        description = "Película encontrada exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con enlaces HATEOAS.
            schema = @Schema(implementation = PeliculaModel.class) // Modelo que se devuelve.
        )
    ),
    // Cuando no se encuentra la película.
    @ApiResponse(
        responseCode = "404",
        description = "Película no encontrada.",
        content = @Content // No se devuelve cuerpo específico.
    )
})

    // Endpoint GET que busca una película por su ID y la devuelve con enlaces HATEOAS.
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PeliculaModel>> ObtenerPorIdPeliculas(@PathVariable Long id) {                                // GET BY ID.
    // Usa el servicio para obtener la película por su ID.
    PeliculaModel pelicula = peliculaService.obtenerPeliculaPorId(id);

    // Si no existe, retorna 404 Not Found.
    if (pelicula == null) {
        return ResponseEntity.notFound().build();
    }
    // Si existe, la envuelve en un EntityModel con enlaces HATEOAS y retorna 200 OK.
    return ResponseEntity.ok(peliculaAssembler.toModel(pelicula));
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir el propósito del endpoint POST.
    @Operation(
        summary = "Crear una nueva película (HATEOAS)", // Título visible en Swagger UI.
        description = "Crea una nueva película en el sistema y devuelve el recurso con enlaces HATEOAS." // Descripción más detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
    // Respuesta cuando la película se crea correctamente.
    @ApiResponse(
        responseCode = "201",
        description = "Película creada correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato HAL con enlaces.
            schema = @Schema(implementation = PeliculaModel.class) // Modelo que documenta el cuerpo.
        )
    ),
    // Respuesta cuando hay error de validación o solicitud malformada.
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud incorrecta. Los datos de la película no son válidos.",
        content = @Content // Sin cuerpo específico.
    )
})

    // Endpoint POST que permite crear una película y devuelve HATEOAS.
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PeliculaModel>> CrearPelicula(@RequestBody PeliculaModel pelicula) {                            // POST.
    // Guarda la nueva película en la base de datos.
    PeliculaModel nuevaPelicula = peliculaService.guardarPelicula(pelicula);

    // Convierte la película en EntityModel con enlaces HATEOAS.
    EntityModel<PeliculaModel> entityModel = peliculaAssembler.toModel(nuevaPelicula);

    // Devuelve 201 Created con:
    // - Header Location apuntando al enlace self del recurso
    // - Cuerpo con los datos + enlaces HATEOAS
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // Ubicación del nuevo recurso.
            .body(entityModel); // Cuerpo de la respuesta.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para explicar el propósito del endpoint.
    @Operation(
        summary = "Actualizar una película (HATEOAS)", // Título que se muestra en Swagger UI.
        description = "Actualiza completamente una película por su ID y devuelve el recurso actualizado con enlaces HATEOAS." // Descripción más detallada.
    )

    // Posibles respuestas del endpoint.
    @ApiResponses(value = {
    // Cuando la película se actualiza correctamente.
    @ApiResponse(
        responseCode = "200",
        description = "Película actualizada correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con enlaces HATEOAS.
            schema = @Schema(implementation = PeliculaModel.class) // Modelo de la película.
        )
    ),
    // Cuando no se encuentra la película.
    @ApiResponse(
        responseCode = "404",
        description = "Película no encontrada.",
        content = @Content // Sin cuerpo específico para 404.
    )
})

    // Endpoint que maneja PUT y devuelve HAL+JSON.
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PeliculaModel>> ActualizarPelicula(                                                           // PUT.
        @PathVariable Long id, // ID de la película a actualizar.
        @RequestBody PeliculaModel peliculaDetails // Nuevos datos que reemplazarán a los anteriores.
    ) {
    // Llama al servicio para actualizar la película.
    PeliculaModel peliculaActualizada = peliculaService.actualizarPelicula(id, peliculaDetails);

    // Si no se encuentra, responde con 404 Not Found.
    if (peliculaActualizada == null) {
        return ResponseEntity.notFound().build();
    }

    // Convierte el objeto actualizado en un EntityModel con enlaces HATEOAS.
    EntityModel<PeliculaModel> entityModel = peliculaAssembler.toModel(peliculaActualizada);

    // Devuelve 200 OK con la película actualizada y enlaces HATEOAS.
    return ResponseEntity.ok(entityModel);
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documenta el propósito del endpoint en Swagger.
    @Operation(
        summary = "Editar parcialmente una película (HATEOAS)", // Título breve para Swagger UI.
        description = "Actualiza parcialmente una película por su ID y devuelve el recurso con enlaces HATEOAS." // Explicación más completa.
    )

    // Define las respuestas esperadas del endpoint.
    @ApiResponses(value = {
    // Cuando la película se actualiza exitosamente.
    @ApiResponse(
        responseCode = "200",
        description = "Película actualizada correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato con enlaces HATEOAS.
            schema = @Schema(implementation = PeliculaModel.class) // Modelo que representa la película.
        )
    ),
    // Cuando no se encuentra la película.
    @ApiResponse(
        responseCode = "404",
        description = "Película no encontrada.",
        content = @Content // No se devuelve cuerpo específico.
    )
})

    // Define el endpoint PATCH que actualiza parcialmente una película y produce HAL+JSON.
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PeliculaModel>> ActualizarParcialPelicula(                                                        // PATCH.
        @PathVariable Long id, // ID de la película a modificar.
        @RequestBody PeliculaModel peliculaDetails // Datos que se desean actualizar (parcialmente).
) {
    // Llama al servicio para hacer la actualización parcial.
    PeliculaModel peliculaActualizada = peliculaService.actualizarPeliculaParcial(id, peliculaDetails);

    // Si no se encuentra la película, responde con 404.
    if (peliculaActualizada == null) {
        return ResponseEntity.notFound().build();
    }

    // Convierte el objeto actualizado en EntityModel con enlaces HATEOAS.
    EntityModel<PeliculaModel> entityModel = peliculaAssembler.toModel(peliculaActualizada);

    // Devuelve 200 OK con el recurso actualizado y enlaces.
    return ResponseEntity.ok(entityModel);
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Eliminar una película existente por su ID (versión HAL + Swagger)
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar película (V2).",
        description = "Elimina una película por su ID en la versión 2 del API."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Película eliminada exitosamente."),
        @ApiResponse(responseCode = "404", description = "Película no encontrada.")
    })
    public ResponseEntity<EntityModel<String>> deletePelicula(@PathVariable Long id) {
        try {
            // Se llama al servicio para eliminar la película correspondiente
            peliculaService.eliminarPeliculaPorId(id);

            // Se construye la respuesta con un mensaje y un link HATEOAS hacia la lista de películas
            EntityModel<String> response = EntityModel.of("Película eliminada exitosamente");
            response.add(linkTo(methodOn(PeliculaControllerV2.class).getAllPeliculas())
                        .withRel("peliculas"));

            // Devuelve un OK 200 con el contenido HATEOAS
            return ResponseEntity.ok(response);
        } catch (RecursoNoEncontradoException e) {
            // Si no se encuentra la película, devuelve un 404
            return ResponseEntity.notFound().build();
        }
    } 
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}