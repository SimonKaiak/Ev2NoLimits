package com.example.NoLimits.Multimedia.controllerV2;

import java.util.List;
import java.util.stream.Collectors;

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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.example.NoLimits.Multimedia.assemblers.AccesorioModelAssembler;
import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.service.AccesorioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
// Swagger ya toma esa info desde el parámetro directamente.
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v2/accesorios",
produces = MediaTypes.HAL_JSON_VALUE) // Especifica que las respuestas seran en formato HAL JSON
// Tag para visualizar en Swagger UI.
@Tag(name = "Accesorios-Controller-V2", description = "Operaciones relacionadas con los accesorios.")
public class AccesorioControllerV2 {

    @Autowired
    private AccesorioService accesorioService;

    @Autowired
    private AccesorioModelAssembler accesorioAssembler;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir la funcionalidad del endpoint.
    @Operation(
        summary = "Obtener todos los accesorios (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene una lista de todos los accesorios con enlaces HATEOAS." // Descripción detallada.
    )

    // Respuesta cuando se obtienen accesorios exitosamente.
    @ApiResponse(
        responseCode = "200", // Código HTTP 200 OK.
        description = "Accesorios obtenidos exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Indica que se devuelve contenido HATEOAS.
            schema = @Schema(implementation = AccesorioModel.class) // Estructura que usará Swagger para mostrar los datos.
        )
    )

    // Respuesta cuando no hay accesorios registrados.
    @ApiResponse(
        responseCode = "204", // Código HTTP 204 No Content.
        description = "No hay accesorios registrados.",
        content = @Content // Sin cuerpo en la respuesta.
    )

    // Endpoint que responde a solicitudes GET y produce HAL+JSON.
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<AccesorioModel>>> getAllAccesorios() {                            // GET ALL.
    // Obtiene la lista de accesorios y los convierte en EntityModel con HATEOAS.
    List<EntityModel<AccesorioModel>> accesorios = accesorioService.obtenerAccesorios().stream()
            .map(accesorioAssembler::toModel) // Aplica el assembler para agregar enlaces HATEOAS.
            .collect(Collectors.toList());

    // Si la lista está vacía, retorna código 204 (sin contenido).
    if (accesorios.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    // Devuelve la colección de accesorios con enlace "self".
    return ResponseEntity.ok(
        CollectionModel.of(
            accesorios, // Lista con los accesorios.
            linkTo(methodOn(AccesorioControllerV2.class).getAllAccesorios()).withSelfRel() // Enlace a sí mismo.
        )
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para indicar el propósito del endpoint.
    @Operation(
        summary = "Buscar accesorio por ID (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene un accesorio específico por su ID con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 200 OK cuando se encuentra el accesorio.
        @ApiResponse(
            responseCode = "200",
            description = "Accesorio encontrado exitosamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Tipo de contenido con enlaces HATEOAS.
                schema = @Schema(implementation = AccesorioModel.class) // Modelo que documenta el contenido devuelto.
            )
        ),
    // Respuesta 404 Not Found cuando no se encuentra el método.
    @ApiResponse(
        responseCode = "404",
        description = "Accesorio no encontrado.",
        content = @Content // Sin contenido específico para 404.
    )
})

    // Endpoint GET que devuelve un accesorio con HATEOAS por su ID.
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AccesorioModel>> getById(@PathVariable Long id) {                                   // GET BY ID.
        return accesorioService.obtenerAccesorioPorId(id) // Busca el accesorio por su ID (Optional).
            .map(accesorioAssembler::toModel) // Si lo encuentra, lo transforma en EntityModel con enlaces HATEOAS.
            .map(ResponseEntity::ok) // Lo envuelve en una respuesta HTTP 200 OK.
            .orElse(ResponseEntity.notFound().build()); // Si no existe, retorna 404 Not Found.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Anotación para Swagger que documenta el propósito del endpoint.
    @Operation(
        summary = "Crear un accesorio (HATEOAS)", // Título breve para Swagger UI.
        description = "Crea un nuevo accesorio en el sistema y devuelve el recurso con enlaces HATEOAS." // Descripción extendida.
    )

    // Documenta las posibles respuestas de este endpoint.
    @ApiResponses(value = {
    // Respuesta 201 Created cuando se crea correctamente.
    @ApiResponse(
        responseCode = "201",
        description = "Accesorio creado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con HATEOAS.
            schema = @Schema(implementation = AccesorioModel.class) // Modelo que se devolverá.
        )
    ),
    // Respuesta 400 Bad Request cuando los datos enviados son inválidos.
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud incorrecta. Los datos del accesorio no son válidos.",
        content = @Content // Sin cuerpo definido.
    )
})

    // Define que este método maneja POST y devuelve un EntityModel con media type HATEOAS.
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AccesorioModel>> crearAccesorio(@RequestBody AccesorioModel accesorio) {                          // POST.
    // Guarda el nuevo accesorio en la base de datos.
    AccesorioModel nuevoAccesorio = accesorioService.guardarAccesorio(accesorio);

    // Lo transforma en un EntityModel con enlaces HATEOAS usando el assembler.
    EntityModel<AccesorioModel> entityModel = accesorioAssembler.toModel(nuevoAccesorio);

    // Retorna una respuesta 201 Created con:
    // - Location apuntando al enlace "self" del recurso creado
    // - El cuerpo del detalle del pedido con enlaces HATEOAS
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // Header Location.
            .body(entityModel); // Cuerpo con HATEOAS.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir el propósito del endpoint PUT.
    @Operation(
        summary = "Actualizar un accesorio (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza completamente un accesorio por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas para el endpoint.
    @ApiResponses(value = {
    // Respuesta exitosa.
    @ApiResponse(
        responseCode = "200",
        description = "Accesorio actualizado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
            schema = @Schema(implementation = AccesorioModel.class) // Modelo que documenta el contenido.
        )
    ),
    // Cuando el recurso no se encuentra.
    @ApiResponse(
        responseCode = "404",
        description = "Accesorio no encontrado.",
        content = @Content // Sin contenido definido para este caso.
    )
})

    // Define que este método maneja PUT y produce contenido HATEOAS.
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AccesorioModel>> actualizarAccesorio(                                                          // PUT.
        @PathVariable Long id, // ID del accesorio que se quiere actualizar.
        @RequestBody AccesorioModel accesorioModel // Datos nuevos para reemplazar completamente al método actual.
    ) {
        try {
            // Llama al servicio para realizar la actualización.
            AccesorioModel accesorioActualizado = accesorioService.actualizarAccesorio(id, accesorioModel);

            // Transforma el resultado en EntityModel con enlaces HATEOAS.
            EntityModel<AccesorioModel> entityModel = accesorioAssembler.toModel(accesorioActualizado);

            // Retorna HTTP 200 OK con el recurso actualizado y enlaces.
            return ResponseEntity.ok(entityModel);
        } catch (RuntimeException e) {
            // Si no encuentra el accesorio, devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documenta el endpoint PATCH para Swagger.
    @Operation(
        summary = "Editar parcialmente un accesorio (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza parcialmente un accesorio por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta exitosa.
        @ApiResponse(
            responseCode = "200",
            description = "Accesorio actualizado correctamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
                schema = @Schema(implementation = AccesorioModel.class) // Modelo que representa el recurso.
            )
        ),
        // Respuesta cuando no se encuentra el accesorio.
        @ApiResponse(
            responseCode = "404",
            description = "Accesorio no encontrado.",
            content = @Content // No se devuelve cuerpo específico.
        )
    })

    // Define que el método maneja PATCH y produce HAL+JSON.
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AccesorioModel>> actualizaAccesorioParcial(                                               // PATCH.
        @PathVariable Long id, // ID del accesorio a actualizar.
        @RequestBody AccesorioModel accesorioDetalles // Datos nuevos (pueden ser parciales).
    ) {
        try {
            // Llama al servicio para actualizar parcialmente el recurso.
            AccesorioModel accesorioActualizado = accesorioService.actualizarAccesorioParcial(id, accesorioDetalles);

            // Lo transforma en un EntityModel con enlaces HATEOAS.
            EntityModel<AccesorioModel> entityModel = accesorioAssembler.toModel(accesorioActualizado);

            // Retorna código 200 OK con el recurso actualizado.
            return ResponseEntity.ok(entityModel);
        } catch (RuntimeException e) {
            // Si no se encuentra el recurso, devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documenta el propósito del endpoint DELETE en Swagger.
    @Operation(
        summary = "Eliminar un accesorio (HATEOAS)", // Título visible en Swagger UI.
        description = "Elimina un accesorio por su ID." // Explicación detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 204 No Content cuando se elimina correctamente.
        @ApiResponse(
            responseCode = "204",
            description = "Accesorio eliminado exitosamente."
        ),
        // Respuesta 404 Not Found si no se encuentra el recurso.
        @ApiResponse(
            responseCode = "404",
            description = "Accesorio no encontrado."
        )
    })

    // Define que este método maneja DELETE, aunque no produce contenido HATEOAS.
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarAccesorio(@PathVariable Long id) {                                                        // DELETE.
        try {
            // Intenta eliminar el accesorio usando el servicio.
            accesorioService.eliminarAccesorioPorId(id);

            // Si se elimina exitosamente, devuelve 204 No Content.
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Si hay una excepción (como que no exista), devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
