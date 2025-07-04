package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.MetodoPagoModelAssembler;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.service.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/metodos-pago",  produces = MediaTypes.HAL_JSON_VALUE) // Especifica que las respuestas seran en formato HAL JSON
@Tag(name = "MétodoPago-Controller-V2", description = "Operaciones relacionadas con los método de pagos.")
public class MetodoPagoControllerV2 {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Autowired
    private MetodoPagoModelAssembler metodoPagoassembler;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir la funcionalidad del endpoint.
    @Operation(
        summary = "Obtener todos los métodos de pago (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene una lista de todos los métodos de pago con enlaces HATEOAS." // Descripción detallada.
    )

    // Respuesta cuando se obtienen métodos de pago exitosamente.
    @ApiResponse(
        responseCode = "200", // Código HTTP 200 OK.
        description = "Métodos de pago obtenidos exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Indica que se devuelve contenido HATEOAS.
            schema = @Schema(implementation = MetodoPagoModel.class) // Estructura que usará Swagger para mostrar los datos.
        )
    )

    // Respuesta cuando no hay métodos de pago registrados.
    @ApiResponse(
        responseCode = "204", // Código HTTP 204 No Content.
        description = "No hay métodos de pago registrados.",
        content = @Content // Sin cuerpo en la respuesta.
    )

    // Endpoint que responde a solicitudes GET y produce HAL+JSON.
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<MetodoPagoModel>>> ObtenerTodosMetodosPago() {                            // GET ALL.
    // Obtiene la lista de métodos de pago y los convierte en EntityModel con HATEOAS.
    List<EntityModel<MetodoPagoModel>> metodosPago = metodoPagoService.findAll().stream()
            .map(metodoPagoassembler::toModel) // Aplica el assembler para agregar enlaces HATEOAS.
            .collect(Collectors.toList());

    // Si la lista está vacía, retorna código 204 (sin contenido).
    if (metodosPago.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    // Devuelve la colección de métodos de pago con enlace "self".
    return ResponseEntity.ok(
        CollectionModel.of(
            metodosPago, // Lista con los métodos de pago.
            linkTo(methodOn(MetodoPagoControllerV2.class).ObtenerTodosMetodosPago()).withSelfRel() // Enlace a sí mismo.
        )
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para indicar el propósito del endpoint.
    @Operation(
        summary = "Buscar método de pago por ID (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene un método de pago específico por su ID con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 200 OK cuando se encuentra el método de pago.
        @ApiResponse(
            responseCode = "200",
            description = "Método de pago encontrado exitosamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Tipo de contenido con enlaces HATEOAS.
                schema = @Schema(implementation = MetodoPagoModel.class) // Modelo que documenta el contenido devuelto.
            )
        ),
    // Respuesta 404 Not Found cuando no se encuentra el método.
    @ApiResponse(
        responseCode = "404",
        description = "Método de pago no encontrado.",
        content = @Content // Sin contenido específico para 404.
    )
})

    // Endpoint GET que devuelve un método de pago con HATEOAS por su ID.
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoModel>> ObtenerMetodoDePagoPorId(@PathVariable Long id) {                                   // GET BY ID.
        return metodoPagoService.findById(id) // Busca el método de pago por su ID (Optional).
            .map(metodoPagoassembler::toModel) // Si lo encuentra, lo transforma en EntityModel con enlaces HATEOAS.
            .map(ResponseEntity::ok) // Lo envuelve en una respuesta HTTP 200 OK.
            .orElse(ResponseEntity.notFound().build()); // Si no existe, retorna 404 Not Found.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Anotación para Swagger que documenta el propósito del endpoint.
    @Operation(
        summary = "Crear un método de pago (HATEOAS)", // Título breve para Swagger UI.
        description = "Crea un nuevo método de pago en el sistema y devuelve el recurso con enlaces HATEOAS." // Descripción extendida.
    )

    // Documenta las posibles respuestas de este endpoint.
    @ApiResponses(value = {
    // Respuesta 201 Created cuando se crea correctamente.
    @ApiResponse(
        responseCode = "201",
        description = "Método de pago creado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con HATEOAS.
            schema = @Schema(implementation = MetodoPagoModel.class) // Modelo que se devolverá.
        )
    ),
    // Respuesta 400 Bad Request cuando los datos enviados son inválidos.
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud incorrecta. Los datos del método de pago no son válidos.",
        content = @Content // Sin cuerpo definido.
    )
})

    // Define que este método maneja POST y devuelve un EntityModel con media type HATEOAS.
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoModel>> CrearMetodoPago(@RequestBody MetodoPagoModel metodoPago) {                          // POST.
    // Guarda el nuevo método de pago en la base de datos.
    MetodoPagoModel nuevoMetodoPago = metodoPagoService.save(metodoPago);

    // Lo transforma en un EntityModel con enlaces HATEOAS usando el assembler.
    EntityModel<MetodoPagoModel> entityModel = metodoPagoassembler.toModel(nuevoMetodoPago);

    // Retorna una respuesta 201 Created con:
    // - Location apuntando al enlace "self" del recurso creado
    // - El cuerpo del método de pago con enlaces HATEOAS
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // Header Location.
            .body(entityModel); // Cuerpo con HATEOAS.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir el propósito del endpoint PUT.
    @Operation(
        summary = "Actualizar un método de pago (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza completamente un método de pago por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas para el endpoint.
    @ApiResponses(value = {
    // Respuesta exitosa.
    @ApiResponse(
        responseCode = "200",
        description = "Método de pago actualizado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
            schema = @Schema(implementation = MetodoPagoModel.class) // Modelo que documenta el contenido.
        )
    ),
    // Cuando el recurso no se encuentra.
    @ApiResponse(
        responseCode = "404",
        description = "Método de pago no encontrado.",
        content = @Content // Sin contenido definido para este caso.
    )
})

    // Define que este método maneja PUT y produce contenido HATEOAS.
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoModel>> Actualizar_MetodoPago(                                                          // PUT.
        @PathVariable Long id, // ID del método de pago que se quiere actualizar.
        @RequestBody MetodoPagoModel metodoPagoDetails // Datos nuevos para reemplazar completamente al método actual.
    ) {
        try {
            // Llama al servicio para realizar la actualización.
            MetodoPagoModel metodoActualizado = metodoPagoService.update(id, metodoPagoDetails);

            // Transforma el resultado en EntityModel con enlaces HATEOAS.
            EntityModel<MetodoPagoModel> entityModel = metodoPagoassembler.toModel(metodoActualizado);

            // Retorna HTTP 200 OK con el recurso actualizado y enlaces.
            return ResponseEntity.ok(entityModel);
        } catch (RuntimeException e) {
            // Si no encuentra el método de pago, devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documenta el endpoint PATCH para Swagger.
    @Operation(
        summary = "Editar parcialmente un método de pago (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza parcialmente un método de pago por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta exitosa.
        @ApiResponse(
            responseCode = "200",
            description = "Método de pago actualizado correctamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
                schema = @Schema(implementation = MetodoPagoModel.class) // Modelo que representa el recurso.
            )
        ),
        // Respuesta cuando no se encuentra el método de pago.
        @ApiResponse(
            responseCode = "404",
            description = "Método de pago no encontrado.",
            content = @Content // No se devuelve cuerpo específico.
        )
    })

    // Define que el método maneja PATCH y produce HAL+JSON.
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoModel>> actualiza_parcial_MetodoPago(                                               // PATCH.
        @PathVariable Long id, // ID del método de pago a actualizar.
        @RequestBody MetodoPagoModel metodoPagoDetails // Datos nuevos (pueden ser parciales).
    ) {
        try {
            // Llama al servicio para actualizar parcialmente el recurso.
            MetodoPagoModel metodoActualizado = metodoPagoService.patch(id, metodoPagoDetails);

            // Lo transforma en un EntityModel con enlaces HATEOAS.
            EntityModel<MetodoPagoModel> entityModel = metodoPagoassembler.toModel(metodoActualizado);

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
        summary = "Eliminar un método de pago (HATEOAS)", // Título visible en Swagger UI.
        description = "Elimina un método de pago por su ID." // Explicación detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 204 No Content cuando se elimina correctamente.
        @ApiResponse(
            responseCode = "204",
            description = "Método de pago eliminado exitosamente."
        ),
        // Respuesta 404 Not Found si no se encuentra el recurso.
        @ApiResponse(
            responseCode = "404",
            description = "Método de pago no encontrado."
        )
    })

    // Define que este método maneja DELETE, aunque no produce contenido HATEOAS.
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> EliminarMetodoPago(@PathVariable Long id) {
        try {
            metodoPagoService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build(); // 404
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
