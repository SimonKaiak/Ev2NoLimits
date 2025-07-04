package com.example.NoLimits.Multimedia.controllerV2;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.assemblers.VideojuegoModelAssembler;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;
import com.example.NoLimits.Multimedia.service.VideojuegoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/api/v2/videojuegos", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Videojuego-Controller-V2", description = "Operaciones relacionadas con los videojuegos.")
public class VideojuegoControllerV2 {

    @Autowired
    private VideojuegoService videojuegoService;

    @Autowired
    private VideojuegoModelAssembler assembler;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger que describe el objetivo del endpoint.
    @Operation(
        summary = "Listar Videojuegos (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene una lista de todos los videojuegos disponibles con enlaces HATEOAS." // Descripción detallada.
    )

    // Respuesta cuando hay videojuegos disponibles.
    @ApiResponse(
        responseCode = "200",
        description = "Lista de videojuegos obtenida exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Indica que se devuelve contenido HAL con HATEOAS.
            schema = @Schema(implementation = VideojuegoModel.class) // Modelo documentado para Swagger.
        )
    )

    // Respuesta cuando no se encuentran videojuegos.
    @ApiResponse(
        responseCode = "204",
        description = "No se encontraron videojuegos.",
        content = @Content // No se devuelve cuerpo.
    )

    // Define que este método maneja GET y produce contenido HAL+JSON.
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    // Quita el Warning verde.
    @SuppressWarnings("unchecked")
    public CollectionModel<EntityModel<VideojuegoModel>> getAllVideojuegos() {                                                              // GET ALL.
    // Obtiene la lista de videojuegos y los transforma a EntityModel con enlaces HATEOAS.
    // List<?>: No importa que variable haya en la lista.
    List<?> listaGenerica = videojuegoService.obtenerVideojuegos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());  

    List<EntityModel<VideojuegoModel>> videojuegos = (List<EntityModel<VideojuegoModel>>) listaGenerica;


    // Devuelve la lista como una CollectionModel con un enlace self.
    return CollectionModel.of(
        videojuegos, // Lista de videojuegos con enlaces.
        linkTo(methodOn(VideojuegoControllerV2.class).getAllVideojuegos()).withSelfRel() // Enlace a este mismo recurso.
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Buscar videojuego por ID (versión HATEOAS con HAL)
    @Operation(
        summary = "Buscar videojuego por ID (HATEOAS)", // Breve descripción en Swagger
        description = "Obtiene un videojuego específico por su ID, con enlaces HATEOAS." // Detalle del endpoint
    )
    @ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Videojuego encontrado exitosamente.",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = VideojuegoModel.class)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Videojuego no encontrado."
    )
})
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE) // Define que produce HAL+JSON
    public ResponseEntity<EntityModel<VideojuegoModel>> getVideojuegoById(@PathVariable Long id) {                                          // GET BY ID.
    // Busca el videojuego por su ID
    VideojuegoModel videojuego = videojuegoService.obtenerVideojuegoPorId(id);                                          

    // Si no existe, responde 404 Not Found
    if (videojuego == null) {
        return ResponseEntity.notFound().build();
    }

    // Si existe, lo envuelve con enlaces HATEOAS y lo devuelve
    return ResponseEntity.ok(assembler.toModel(videojuego));
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Crear un nuevo videojuego (versión HAL + Swagger)
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE) // Define que la respuesta será en formato HAL JSON
    @Operation(
        summary = "Guardar videojuego (HATEOAS)", // Breve resumen del endpoint
        description = "Guarda un nuevo videojuego en la base de datos y devuelve el recurso con enlaces HATEOAS." // Detalle del endpoint
    )
    @ApiResponses(value = {
    @ApiResponse(
        responseCode = "201", // Código 201 = Created
        description = "Videojuego guardado exitosamente.",
        content = @Content(
            mediaType = "application/hal+json", // Indica que se devuelve HAL+JSON
            schema = @Schema(implementation = VideojuegoModel.class) // Indica el tipo de objeto en la respuesta
        )
    ),
    @ApiResponse(
        responseCode = "400", // Código 400 = Bad Request
        description = "Error al guardar el videojuego."
    )
})
    public ResponseEntity<EntityModel<VideojuegoModel>> crearVideojuego(@RequestBody VideojuegoModel videojuego) {                          // POST.
    // Guarda el videojuego utilizando el servicio
    VideojuegoModel createdVideojuego = videojuegoService.guardarVideojuego(videojuego);

    // Convierte el modelo en EntityModel con enlaces HATEOAS
    EntityModel<VideojuegoModel> entityModel = assembler.toModel(createdVideojuego);

    // Devuelve una respuesta 201 Created con la URI del recurso recién creado
    return ResponseEntity
            .created(
                linkTo(methodOn(VideojuegoControllerV2.class)
                    .getVideojuegoById(createdVideojuego.getIdVideojuego())) // Enlace al recurso recién creado
                    .toUri()
            )
            .body(entityModel); // Incluye el objeto creado con sus enlaces
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualizar un videojuego existente (versión HAL + Swagger)
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE) // Define el endpoint PUT con respuesta HAL
    @Operation(
        summary = "Actualizar Videojuego (HATEOAS)", // Título descriptivo del endpoint
        description = "Actualiza un videojuego existente por su ID y devuelve el recurso actualizado con enlaces HATEOAS."
    )
    @ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", // Código 200 OK
        description = "Videojuego actualizado exitosamente.",
        content = @Content(
            mediaType = "application/hal+json", // HAL+JSON como formato de respuesta
            schema = @Schema(implementation = VideojuegoModel.class) // Tipo de objeto esperado
        )
    ),
    @ApiResponse(
        responseCode = "404", // Código 404 Not Found
        description = "Videojuego no encontrado."
    )
})
    public ResponseEntity<EntityModel<VideojuegoModel>> updateVideoJuego(                                                               // PUT.
        @PathVariable Long id, // Recibe el ID del videojuego desde la URL
        @RequestBody VideojuegoModel videojuego) { // Recibe el cuerpo del videojuego a actualizar

    // Llama al servicio para actualizar el videojuego
    VideojuegoModel updatedVideojuego = videojuegoService.actualizarVideojuego(id, videojuego);

    // Si no existe el videojuego, retorna 404
    if (updatedVideojuego == null) {
        return ResponseEntity.notFound().build();
    }

    // Transforma el objeto actualizado en un modelo HAL con enlaces
    EntityModel<VideojuegoModel> entityModel = assembler.toModel(updatedVideojuego);

    // Retorna el recurso con estado 200 OK y su representación HAL
    return ResponseEntity.ok(entityModel);
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualiza parcialmente un videojuego por su ID (versión HAL + Swagger)
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE) // Define el endpoint PATCH con respuesta HAL
    @Operation(
        summary = "Actualizar videojuego parcialmente.", // Título del endpoint
        description = "Actualiza parcialmente un videojuego existente por su ID." // Explicación del endpoint
    )
    @ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", // Código 200 OK
        description = "Videojuego actualizado parcialmente exitosamente.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class))
    ),
    @ApiResponse(
        responseCode = "404", // Código 404 Not Found
        description = "Videojuego no encontrado.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideojuegoModel.class))
    )
})
    public ResponseEntity<EntityModel<VideojuegoModel>> patchVideojuego(                                                                    // PATCH.
        @PathVariable Long id, // ID del videojuego a actualizar
        @RequestBody VideojuegoModel detallesParciales // Datos parciales a actualizar
) {
    try {
        // Llama al servicio para aplicar la actualización parcial
        VideojuegoModel videojuegoActualizado = videojuegoService.actualizarVideojuegoParcial(id, detallesParciales);

        // Transforma el modelo en un EntityModel con enlaces HAL
        EntityModel<VideojuegoModel> entityModel = assembler.toModel(videojuegoActualizado);

        // Devuelve respuesta 200 OK con el EntityModel
        return ResponseEntity.ok(entityModel);

    } catch (RuntimeException e) {
        // Si no se encuentra el videojuego, retorna 404
        return ResponseEntity.notFound().build();
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Eliminar un videojuego existente por su ID (versión HAL + Swagger)
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE) // Define el endpoint DELETE con respuesta HAL
    @Operation(
        summary = "Eliminar videojuego.", // Título del endpoint
        description = "Elimina un videojuego existente por su ID." // Explicación del endpoint
    )
    @ApiResponses(value = {
    @ApiResponse(
        responseCode = "204", // Código 204 No Content
        description = "Videojuego eliminado exitosamente."
    ),
    @ApiResponse(
        responseCode = "404", // Código 404 Not Found
        description = "Videojuego no encontrado."
    )
})
    public ResponseEntity<?> deleteVideoJuego(@PathVariable Long id) { // Recibe el ID del videojuego desde la URL                          // DELETE.
    try {
        videojuegoService.eliminarVideojuegoPorId(id); // Llama al servicio para eliminar el videojuego
        return ResponseEntity.noContent().build(); // Retorna 204 No Content si se eliminó correctamente
    } catch (Exception e) {
        return ResponseEntity.notFound().build(); // Si falla (ej: no existe), retorna 404 Not Found
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
