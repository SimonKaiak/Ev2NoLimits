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

import com.example.NoLimits.Multimedia.assemblers.DetalleVentaModelAssembler;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.service.DetalleVentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v2/detalles-venta", produces = MediaTypes.HAL_JSON_VALUE) // Especifica que las respuestas seran en formato HAL JSON
@Tag(name = "DetalleVenta-Controller-V2", description = "Operaciones relacionadas con los detalles de las ventas.")
public class DetalleVentaControllerV2 {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private DetalleVentaModelAssembler detalleVentaModelAssembler;

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
    // Documentación Swagger para describir la funcionalidad del endpoint.
    @Operation(
        summary = "Obtener todos los detalles de la venta (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene una lista de todos los detalles de la venta con enlaces HATEOAS." // Descripción detallada.
    )

    // Respuesta cuando se obtienen detalles de ventas exitosamente.
    @ApiResponse(
        responseCode = "200", // Código HTTP 200 OK.
        description = "Detalles de ventas obtenidos exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Indica que se devuelve contenido HATEOAS.
            schema = @Schema(implementation = DetalleVentaModel.class) // Estructura que usará Swagger para mostrar los datos.
        )
    )

    // Respuesta cuando no hay detalles de ventas registrados.
    @ApiResponse(
        responseCode = "204", // Código HTTP 204 No Content.
        description = "No hay detalles de ventas registrados.",
        content = @Content // Sin cuerpo en la respuesta.
    )

    // Endpoint que responde a solicitudes GET y produce HAL+JSON.
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<DetalleVentaModel>>> GetAllDetallesVenta() {                            // GET ALL.
    // Obtiene la lista de detalles de ventas y los convierte en EntityModel con HATEOAS.
    List<EntityModel<DetalleVentaModel>> detalleVentas = detalleVentaService.findAll().stream()
            .map(detalleVentaModelAssembler::toModel) // Aplica el assembler para agregar enlaces HATEOAS.
            .collect(Collectors.toList());

    // Si la lista está vacía, retorna código 204 (sin contenido).
    if (detalleVentas.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    // Devuelve la colección de detalles de ventas con enlace "self".
    return ResponseEntity.ok(
        CollectionModel.of(
            detalleVentas, // Lista con los detalles de los ventas.
            linkTo(methodOn(DetalleVentaControllerV2.class).GetAllDetallesVenta()).withSelfRel() // Enlace a sí mismo.
        )
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para indicar el propósito del endpoint.
    @Operation(
        summary = "Buscar detalle de venta por ID (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene un detalle de venta específico por su ID con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 200 OK cuando se encuentra el detalle de venta.
        @ApiResponse(
            responseCode = "200",
            description = "Detalle de venta encontrado exitosamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Tipo de contenido con enlaces HATEOAS.
                schema = @Schema(implementation = DetalleVentaModel.class) // Modelo que documenta el contenido devuelto.
            )
        ),
    // Respuesta 404 Not Found cuando no se encuentra el método.
    @ApiResponse(
        responseCode = "404",
        description = "Detalle de venta no encontrado.",
        content = @Content // Sin contenido específico para 404.
    )
})

    // Endpoint GET que devuelve un tipo de producto con HATEOAS por su ID.
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetalleVentaModel>> GetById(@PathVariable Long id) {                                   // GET BY ID.
        return detalleVentaService.findById(id) // Busca el detalle de pedido por su ID (Optional).
            .map(detalleVentaModelAssembler::toModel) // Si lo encuentra, lo transforma en EntityModel con enlaces HATEOAS.
            .map(ResponseEntity::ok) // Lo envuelve en una respuesta HTTP 200 OK.
            .orElse(ResponseEntity.notFound().build()); // Si no existe, retorna 404 Not Found.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Anotación para Swagger que documenta el propósito del endpoint.
    @Operation(
        summary = "Crear un detalle de venta (HATEOAS)", // Título breve para Swagger UI.
        description = "Crea un nuevo detalle de venta en el sistema y devuelve el recurso con enlaces HATEOAS." // Descripción extendida.
    )

    // Documenta las posibles respuestas de este endpoint.
    @ApiResponses(value = {
    // Respuesta 201 Created cuando se crea correctamente.
    @ApiResponse(
        responseCode = "201",
        description = "Detalle de venta creado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con HATEOAS.
            schema = @Schema(implementation = DetalleVentaModel.class) // Modelo que se devolverá.
        )
    ),
    // Respuesta 400 Bad Request cuando los datos enviados son inválidos.
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud incorrecta. Los datos del detalle de la venta no son válidos.",
        content = @Content // Sin cuerpo definido.
    )
})

    // Define que este método maneja POST y devuelve un EntityModel con media type HATEOAS.
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetalleVentaModel>> CrearDetalleVenta(@RequestBody DetalleVentaModel detalleVenta) {                          // POST.
    // Guarda el nuevo detalle de venta en la base de datos.
    DetalleVentaModel nuevoDetalleVenta = detalleVentaService.save(detalleVenta);

    // Lo transforma en un EntityModel con enlaces HATEOAS usando el assembler.
    EntityModel<DetalleVentaModel> entityModel = detalleVentaModelAssembler.toModel(nuevoDetalleVenta); // ✅

    // Retorna una respuesta 201 Created con:
    // - Location apuntando al enlace "self" del recurso creado
    // - El cuerpo del detalle de la venta con enlaces HATEOAS
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // Header Location.
            .body(entityModel); // Cuerpo con HATEOAS.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir el propósito del endpoint PUT.
    @Operation(
        summary = "Actualizar un detalle de venta (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza completamente un detalle de venta por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas para el endpoint.
    @ApiResponses(value = {
    // Respuesta exitosa.
    @ApiResponse(
        responseCode = "200",
        description = "Detalle de venta actualizado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
            schema = @Schema(implementation = DetalleVentaModel.class) // Modelo que documenta el contenido.
        )
    ),
    // Cuando el recurso no se encuentra.
    @ApiResponse(
        responseCode = "404",
        description = "Detalle de venta no encontrado.",
        content = @Content // Sin contenido definido para este caso.
    )
})

    // Define que este método maneja PUT y produce contenido HATEOAS.
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetalleVentaModel>> ActualizarDetalleVenta(                                                          // PUT.
        @PathVariable Long id, // ID del detalle de venta que se quiere actualizar.
        @RequestBody DetalleVentaModel detalleVentaModel // Datos nuevos para reemplazar completamente al método actual.
    ) {
        try {
            // Llama al servicio para realizar la actualización.
            DetalleVentaModel detalleVentaActualizado = detalleVentaService.update(id, detalleVentaModel);

            // Transforma el resultado en EntityModel con enlaces HATEOAS.
            EntityModel<DetalleVentaModel> entityModel = detalleVentaModelAssembler.toModel(detalleVentaActualizado);

            // Retorna HTTP 200 OK con el recurso actualizado y enlaces.
            return ResponseEntity.ok(entityModel);
        } catch (RuntimeException e) {
            // Si no encuentra el detalle de venta, devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documenta el endpoint PATCH para Swagger.
    @Operation(
        summary = "Editar parcialmente el detalle de una venta (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza parcialmente el detalle de una venta por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta exitosa.
        @ApiResponse(
            responseCode = "200",
            description = "Detalle de venta actualizado correctamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
                schema = @Schema(implementation = DetalleVentaModel.class) // Modelo que representa el recurso.
            )
        ),
        // Respuesta cuando no se encuentra el detalle de una venta.
        @ApiResponse(
            responseCode = "404",
            description = "Detalle de venta no encontrado.",
            content = @Content // No se devuelve cuerpo específico.
        )
    })

    // Define que el método maneja PATCH y produce HAL+JSON.
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetalleVentaModel>> actualizaDetalleVentaParcial(                                               // PATCH.
        @PathVariable Long id, // ID del detalle de venta a actualizar.
        @RequestBody DetalleVentaModel detalleVentaDetalles // Datos nuevos (pueden ser parciales).
    ) {
        try {
            // Llama al servicio para actualizar parcialmente el recurso.
            DetalleVentaModel detalleVentaActualizado = detalleVentaService.patchDetalleVentaModel(id, detalleVentaDetalles);

            // Lo transforma en un EntityModel con enlaces HATEOAS.
            EntityModel<DetalleVentaModel> entityModel = detalleVentaModelAssembler.toModel(detalleVentaActualizado);

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
        summary = "Eliminar el detalle de una venta (HATEOAS)", // Título visible en Swagger UI.
        description = "Elimina el detalle de una venta por su ID." // Explicación detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 204 No Content cuando se elimina correctamente.
        @ApiResponse(
            responseCode = "204",
            description = "Detalle de venta eliminado exitosamente."
        ),
        // Respuesta 404 Not Found si no se encuentra el recurso.
        @ApiResponse(
            responseCode = "404",
            description = "Detalle de venta no encontrado."
        )
    })

    // Define que este método maneja DELETE, aunque no produce contenido HATEOAS.
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> EliminarDetalleVenta(@PathVariable Long id) {                                                        // DELETE.
        try {
            // Intenta eliminar el detalle de venta usando el servicio.
            detalleVentaService.deleteById(id);

            // Si se elimina exitosamente, devuelve 204 No Content.
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Si hay una excepción (como que no exista), devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}