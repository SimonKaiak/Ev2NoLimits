package com.example.NoLimits.Multimedia.controllerV2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.example.NoLimits.Multimedia.assemblers.DetallePedidoModelAssembler;
import com.example.NoLimits.Multimedia.model.DetallePedidoModel;
import com.example.NoLimits.Multimedia.service.DetallePedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v2/detalles_pedidos", produces = MediaTypes.HAL_JSON_VALUE) // Especifica que las respuestas seran en formato HAL JSON
@Tag(name = "DetallePedido-Controller-V2", description = "Operaciones relacionadas con los detalles del pedido.")
public class DetallePedidoControllerV2 {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Autowired
    private DetallePedidoModelAssembler detallePedidoAssembler;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir la funcionalidad del endpoint.
    @Operation(
        summary = "Obtener todos los detalles del pedido (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene una lista de todos los detalles del pedido con enlaces HATEOAS." // Descripción detallada.
    )

    // Respuesta cuando se obtienen detalles de pedidos exitosamente.
    @ApiResponse(
        responseCode = "200", // Código HTTP 200 OK.
        description = "Detalles de pedidos obtenidos exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Indica que se devuelve contenido HATEOAS.
            schema = @Schema(implementation = DetallePedidoModel.class) // Estructura que usará Swagger para mostrar los datos.
        )
    )

    // Respuesta cuando no hay detalles de pedidos registrados.
    @ApiResponse(
        responseCode = "204", // Código HTTP 204 No Content.
        description = "No hay detalles de pedidos registrados.",
        content = @Content // Sin cuerpo en la respuesta.
    )

    // Endpoint que responde a solicitudes GET y produce HAL+JSON.
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<DetallePedidoModel>>> GetAllDetallesPedidos() {                            // GET ALL.
    // Obtiene la lista de detalles de pedidos y los convierte en EntityModel con HATEOAS.
    List<EntityModel<DetallePedidoModel>> detallePedidos = detallePedidoService.findAll().stream()
            .map(detallePedidoAssembler::toModel) // Aplica el assembler para agregar enlaces HATEOAS.
            .collect(Collectors.toList());

    // Si la lista está vacía, retorna código 204 (sin contenido).
    if (detallePedidos.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    // Devuelve la colección de detalles de pedidos con enlace "self".
    return ResponseEntity.ok(
        CollectionModel.of(
            detallePedidos, // Lista con los detalles de los pedidos.
            linkTo(methodOn(DetallePedidoControllerV2.class).GetAllDetallesPedidos()).withSelfRel() // Enlace a sí mismo.
        )
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para indicar el propósito del endpoint.
    @Operation(
        summary = "Buscar detalle de pedido por ID (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene un detalle de pedido específico por su ID con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 200 OK cuando se encuentra el detalle de pedido.
        @ApiResponse(
            responseCode = "200",
            description = "Detalle de pedido encontrado exitosamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Tipo de contenido con enlaces HATEOAS.
                schema = @Schema(implementation = DetallePedidoModel.class) // Modelo que documenta el contenido devuelto.
            )
        ),
    // Respuesta 404 Not Found cuando no se encuentra el método.
    @ApiResponse(
        responseCode = "404",
        description = "Detalle de pedido no encontrado.",
        content = @Content // Sin contenido específico para 404.
    )
})

    // Endpoint GET que devuelve un tipo de producto con HATEOAS por su ID.
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetallePedidoModel>> GetById(@PathVariable Long id) {                                   // GET BY ID.
        return detallePedidoService.findById(id) // Busca el detalle de pedido por su ID (Optional).
            .map(detallePedidoAssembler::toModel) // Si lo encuentra, lo transforma en EntityModel con enlaces HATEOAS.
            .map(ResponseEntity::ok) // Lo envuelve en una respuesta HTTP 200 OK.
            .orElse(ResponseEntity.notFound().build()); // Si no existe, retorna 404 Not Found.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Anotación para Swagger que documenta el propósito del endpoint.
    @Operation(
        summary = "Crear un detalle de pedido (HATEOAS)", // Título breve para Swagger UI.
        description = "Crea un nuevo detalle de pedido en el sistema y devuelve el recurso con enlaces HATEOAS." // Descripción extendida.
    )

    // Documenta las posibles respuestas de este endpoint.
    @ApiResponses(value = {
    // Respuesta 201 Created cuando se crea correctamente.
    @ApiResponse(
        responseCode = "201",
        description = "Detalle de pedido creado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con HATEOAS.
            schema = @Schema(implementation = DetallePedidoModel.class) // Modelo que se devolverá.
        )
    ),
    // Respuesta 400 Bad Request cuando los datos enviados son inválidos.
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud incorrecta. Los datos del detalle del pedido no son válidos.",
        content = @Content // Sin cuerpo definido.
    )
})

    // Define que este método maneja POST y devuelve un EntityModel con media type HATEOAS.
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetallePedidoModel>> CrearDetallePedido(@RequestBody DetallePedidoModel detallePedido) {                          // POST.
    // Guarda el nuevo detalle de pedido en la base de datos.
    DetallePedidoModel nuevoDetallePedido = detallePedidoService.save(detallePedido);

    // Lo transforma en un EntityModel con enlaces HATEOAS usando el assembler.
    EntityModel<DetallePedidoModel> entityModel = detallePedidoAssembler.toModel(nuevoDetallePedido);

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
        summary = "Actualizar un detalle de pedido (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza completamente un detalle de pedido por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas para el endpoint.
    @ApiResponses(value = {
    // Respuesta exitosa.
    @ApiResponse(
        responseCode = "200",
        description = "Detalle de pedido actualizado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
            schema = @Schema(implementation = DetallePedidoModel.class) // Modelo que documenta el contenido.
        )
    ),
    // Cuando el recurso no se encuentra.
    @ApiResponse(
        responseCode = "404",
        description = "Detalle de pedido no encontrado.",
        content = @Content // Sin contenido definido para este caso.
    )
})

    // Define que este método maneja PUT y produce contenido HATEOAS.
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetallePedidoModel>> ActualizarDetallePedido(                                                          // PUT.
        @PathVariable Long id, // ID del detalle de pedido que se quiere actualizar.
        @RequestBody DetallePedidoModel detallePedidoModel // Datos nuevos para reemplazar completamente al método actual.
    ) {
        try {
            // Llama al servicio para realizar la actualización.
            DetallePedidoModel detallePedidoActualizado = detallePedidoService.update(id, detallePedidoModel);

            // Transforma el resultado en EntityModel con enlaces HATEOAS.
            EntityModel<DetallePedidoModel> entityModel = detallePedidoAssembler.toModel(detallePedidoActualizado);

            // Retorna HTTP 200 OK con el recurso actualizado y enlaces.
            return ResponseEntity.ok(entityModel);
        } catch (RuntimeException e) {
            // Si no encuentra el detalle de pedido, devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documenta el endpoint PATCH para Swagger.
    @Operation(
        summary = "Editar parcialmente un detalle de pedido (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza parcialmente un detalle de pedido por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta exitosa.
        @ApiResponse(
            responseCode = "200",
            description = "Detalle de pedido actualizado correctamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
                schema = @Schema(implementation = DetallePedidoModel.class) // Modelo que representa el recurso.
            )
        ),
        // Respuesta cuando no se encuentra el detalle de un pedido.
        @ApiResponse(
            responseCode = "404",
            description = "Detalle de pedido no encontrado.",
            content = @Content // No se devuelve cuerpo específico.
        )
    })

    // Define que el método maneja PATCH y produce HAL+JSON.
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetallePedidoModel>> actualizaDetallePedidoParcial(                                               // PATCH.
        @PathVariable Long id, // ID del detalle del pedido a actualizar.
        @RequestBody DetallePedidoModel detallePedidoDetalles // Datos nuevos (pueden ser parciales).
    ) {
        try {
            // Llama al servicio para actualizar parcialmente el recurso.
            DetallePedidoModel detallePedidoActualizado = detallePedidoService.patch(id, detallePedidoDetalles);

            // Lo transforma en un EntityModel con enlaces HATEOAS.
            EntityModel<DetallePedidoModel> entityModel = detallePedidoAssembler.toModel(detallePedidoActualizado);

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
        summary = "Eliminar un detalle de pedido (HATEOAS)", // Título visible en Swagger UI.
        description = "Elimina un detalle de pedido por su ID." // Explicación detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 204 No Content cuando se elimina correctamente.
        @ApiResponse(
            responseCode = "204",
            description = "Detalle de pedido eliminado exitosamente."
        ),
        // Respuesta 404 Not Found si no se encuentra el recurso.
        @ApiResponse(
            responseCode = "404",
            description = "Detalle de pedido no encontrado."
        )
    })

    // Define que este método maneja DELETE, aunque no produce contenido HATEOAS.
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> EliminarDetallePedido(@PathVariable Long id) {                                                        // DELETE.
        try {
            // Intenta eliminar el detalle de pedido usando el servicio.
            detallePedidoService.deleteById(id);

            // Si se elimina exitosamente, devuelve 204 No Content.
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Si hay una excepción (como que no exista), devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
