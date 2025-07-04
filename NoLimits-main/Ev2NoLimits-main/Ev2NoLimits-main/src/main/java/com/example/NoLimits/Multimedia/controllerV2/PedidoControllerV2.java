package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia.assemblers.PedidoModelAssembler;
import com.example.NoLimits.Multimedia.model.PedidoModel;
import com.example.NoLimits.Multimedia.service.PedidoService;

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
@RequestMapping(value = "/api/v2/pedidos", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Pedido-Controller-V2", description = "Operaciones relacionadas con los pedidos.")
public class PedidoControllerV2 {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoModelAssembler pedidoAssembler;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir la funcionalidad del endpoint.
    @Operation(
        summary = "Obtener todos los pedidos (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene una lista de todos los pedidos con enlaces HATEOAS." // Descripción más completa.
    )

    // Respuesta 200 cuando hay pedidos disponibles.
    @ApiResponse(
        responseCode = "200",
        description = "Pedidos encontrados exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido HATEOAS.
            schema = @Schema(implementation = PedidoModel.class) // Modelo de cada pedido.
        )
    )

    // Respuesta 204 cuando no hay pedidos en la base de datos.
    @ApiResponse(
        responseCode = "204",
        description = "No hay pedidos registrados.",
        content = @Content // Sin cuerpo en la respuesta.
    )

    // Define que este endpoint maneja GET y produce contenido con enlaces HATEOAS.
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PedidoModel>>> ObtenerTodosLosPedidos() {                                             // GET ALL.
        // Llama al servicio y convierte cada PedidoModel en EntityModel con enlaces HATEOAS.
        List<EntityModel<PedidoModel>> pedidos = pedidoService.ObtenerTodosLosPedidos().stream().map(pedidoAssembler::toModel).collect(Collectors.toList());

    // Si la lista está vacía, retorna HTTP 204 No Content.
    if (pedidos.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    // Si hay resultados, devuelve HTTP 200 OK con una colección de pedidos y enlace self.
    return ResponseEntity.ok(
        CollectionModel.of(
            pedidos,
            linkTo(methodOn(PedidoControllerV2.class).ObtenerTodosLosPedidos()).withSelfRel() // Enlace a sí mismo.
        )
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger que explica el objetivo del endpoint.
    @Operation(
        summary = "Buscar pedido por ID (HATEOAS)", // Título para Swagger UI.
        description = "Obtiene un pedido específico por su ID con enlaces HATEOAS." // Explicación extendida.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta exitosa cuando se encuentra el pedido.
        @ApiResponse(
            responseCode = "200",
            description = "Pedido encontrado exitosamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Formato con enlaces HAL.
                schema = @Schema(implementation = PedidoModel.class) // Modelo representado.
            )
    ),
    // Respuesta cuando no se encuentra el pedido.
    @ApiResponse(
        responseCode = "404",
        description = "Pedido no encontrado.",
        content = @Content // No se devuelve cuerpo.
    )
})

    // Define el endpoint GET con respuesta HAL+JSON.
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PedidoModel>> BuscarPedidoPorId(@PathVariable Long id) {                                          // GET BY ID.
    // Busca el pedido por ID y, si existe, lo transforma en EntityModel con enlaces.
    return pedidoService.ObtenerPedidoPorId(id)
            .map(pedidoAssembler::toModel) // Aplica HATEOAS si se encuentra.
            .map(ResponseEntity::ok) // Devuelve 200 OK con el pedido y enlaces.
            .orElse(ResponseEntity.notFound().build()); // Si no existe, devuelve 404.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir el propósito del endpoint.
    @Operation(
        summary = "Crear un nuevo pedido (HATEOAS)", // Título visible en Swagger UI.
        description = "Crea un nuevo pedido en el sistema y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las respuestas posibles.
    @ApiResponses(value = {
        // Respuesta cuando se crea correctamente.
        @ApiResponse(
            responseCode = "201",
            description = "Pedido creado correctamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Formato con HATEOAS.
                schema = @Schema(implementation = PedidoModel.class) // Modelo de pedido documentado.
        )
    ),
    // Respuesta si los datos del pedido son inválidos.
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud incorrecta. Los datos del pedido no son válidos.",
        content = @Content // No se muestra cuerpo específico.
    )
})

    // Define el endpoint POST que produce HAL+JSON.
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PedidoModel>> CrearPedido(@RequestBody PedidoModel pedido) {                                      // POST.
    // Guarda el nuevo pedido usando el servicio.
    PedidoModel nuevoPedido = pedidoService.guardarPedido(pedido);

    // Convierte el pedido en EntityModel con enlaces HATEOAS.
    EntityModel<PedidoModel> entityModel = pedidoAssembler.toModel(nuevoPedido);

    // Devuelve 201 Created con:
    // - Header Location apuntando al enlace self
    // - Cuerpo con los datos del nuevo pedido + enlaces
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir el objetivo del endpoint.
    @Operation(
        summary = "Actualizar un pedido (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza completamente un pedido por su ID y devuelve el recurso actualizado con enlaces HATEOAS." // Descripción extendida.
    )

    // Define las posibles respuestas del endpoint.
    @ApiResponses(value = {
    // Respuesta cuando se actualiza exitosamente.
    @ApiResponse(
        responseCode = "200",
        description = "Pedido actualizado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato HAL+JSON con enlaces HATEOAS.
            schema = @Schema(implementation = PedidoModel.class) // Modelo usado para la documentación.
        )
    ),
    // Respuesta cuando el pedido no existe.
    @ApiResponse(
        responseCode = "404",
        description = "Pedido no encontrado.",
        content = @Content // No se devuelve cuerpo.
    )
})

    // Define que este método maneja PUT y produce HAL+JSON.
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PedidoModel>> ActualizarPedido(
        @PathVariable Long id, // ID del pedido a actualizar.
        @RequestBody PedidoModel pedidoDetails // Datos nuevos para reemplazar los anteriores.
) {
    try {
        // Llama al servicio para actualizar el pedido.
        PedidoModel pedidoActualizado = pedidoService.actualizarPedido(id, pedidoDetails);                                      // PUT.

        // Convierte el pedido actualizado en EntityModel con enlaces HATEOAS.
        EntityModel<PedidoModel> entityModel = pedidoAssembler.toModel(pedidoActualizado);

        // Devuelve HTTP 200 OK con el recurso actualizado.
        return ResponseEntity.ok(entityModel);

    } catch (RuntimeException e) {
        // Si no se encuentra el pedido, devuelve 404 Not Found.
        return ResponseEntity.notFound().build();
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger que describe el propósito del endpoint PATCH.
    @Operation(
        summary = "Editar parcialmente un pedido (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza parcialmente un pedido por su ID y devuelve el recurso actualizado con enlaces HATEOAS." // Explicación extendida.
    )

    // Define las posibles respuestas del endpoint.
    @ApiResponses(value = {
    // Respuesta exitosa.
    @ApiResponse(
        responseCode = "200",
        description = "Pedido actualizado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato con enlaces HATEOAS.
            schema = @Schema(implementation = PedidoModel.class) // Modelo de pedido usado en Swagger.
        )
    ),
    // Cuando no se encuentra el recurso.
    @ApiResponse(
        responseCode = "404",
        description = "Pedido no encontrado.",
        content = @Content // No se devuelve cuerpo específico.
    )
})

    // Define el endpoint PATCH que actualiza parcialmente un pedido y devuelve HAL+JSON.
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PedidoModel>> actualizar_parcial_pelicula(
        @PathVariable Long id, // ID del pedido a modificar.
        @RequestBody PedidoModel pedidoDetails // Campos parciales a modificar.
) {
    try {
        // Actualiza el pedido parcialmente a través del servicio.
        PedidoModel pedidoActualizado = pedidoService.ActualizarPedidoParcial(id, pedidoDetails);                                   // PATCH.

        // Lo transforma en EntityModel con enlaces HATEOAS.
        EntityModel<PedidoModel> entityModel = pedidoAssembler.toModel(pedidoActualizado);

        // Devuelve 200 OK con el recurso actualizado y sus enlaces.
        return ResponseEntity.ok(entityModel);

    } catch (RuntimeException e) {
        // Si el pedido no existe, retorna 404 Not Found.
        return ResponseEntity.notFound().build();
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger que describe el propósito del endpoint DELETE.
    @Operation(
        summary = "Eliminar un pedido (HATEOAS)", // Título para Swagger UI.
        description = "Elimina un pedido por su ID." // Explicación clara.
    )

    // Define las posibles respuestas de este endpoint.
    @ApiResponses(value = {
    // Cuando la eliminación es exitosa.
    @ApiResponse(
        responseCode = "204",
        description = "Pedido eliminado exitosamente."
    ),
    // Cuando no se encuentra el pedido.
    @ApiResponse(
        responseCode = "404",
        description = "Pedido no encontrado.",
        content = @Content // Sin cuerpo devuelto.
    )
})

    // Define que este método maneja DELETE y produce HAL+JSON (aunque no devuelve cuerpo).
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> EliminarPedido(@PathVariable Long id) {                                                                // DELETE.
    try {
        // Intenta eliminar el pedido con el servicio.
        pedidoService.eliminarPedido(id);

        // Si se elimina correctamente, devuelve 204 No Content.
        return ResponseEntity.noContent().build();

    } catch (Exception e) {
        // Si el pedido no existe o hay error, devuelve 404 Not Found.
        return ResponseEntity.notFound().build();
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
