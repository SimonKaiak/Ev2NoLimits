package com.example.NoLimits.Multimedia.controllerV2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
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

import com.example.NoLimits.Multimedia.assemblers.TipoProductoModelAssembler;
import com.example.NoLimits.Multimedia.model.TipoProductoModel;
import com.example.NoLimits.Multimedia.service.TipoProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v2/tipos-producto", produces = MediaTypes.HAL_JSON_VALUE) // Especifica que las respuestas seran en formato HAL JSON
@Tag(name = "TipoProducto-Controller-V2", description = "Operaciones relacionadas con los tipos de productos.")
public class TipoProductoControllerV2 {
    
    @Autowired
    private TipoProductoService tipoProductoService;

    @Autowired
    private TipoProductoModelAssembler tipoProductoModelAssembler;

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
    // Documentación Swagger para describir la funcionalidad del endpoint.
    @Operation(
        summary = "Obtener todos los tipos de producto de la venta (HATEOAS)", // Título visible en Swagger UI.
        description = "Obtiene una lista de tipos de producto con enlaces HATEOAS." // Descripción detallada.
    )

    // Respuesta cuando se obtienen tipos de productos exitosamente.
    @ApiResponse(
        responseCode = "200", // Código HTTP 200 OK.
        description = "Tipos de productos obtenidos exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Indica que se devuelve contenido HATEOAS.
            schema = @Schema(implementation = TipoProductoModel.class) // Estructura que usará Swagger para mostrar los datos.
        )
    )

    // Respuesta cuando no hay detalles de ventas registrados.
    @ApiResponse(
        responseCode = "204", // Código HTTP 204 No Content.
        description = "No hay tipos de productos registrados.",
        content = @Content // Sin cuerpo en la respuesta.
    )

    // Endpoint que responde a solicitudes GET y produce HAL+JSON.
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<TipoProductoModel>>> GetAllTipoProductos() {                            // GET ALL.
    // Obtiene la lista de tipos de productos y los convierte en EntityModel con HATEOAS.
    List<EntityModel<TipoProductoModel>> tipoProductos = tipoProductoService.obtenerTiposProductos().stream()
            .map(tipoProductoModelAssembler::toModel) // Aplica el assembler para agregar enlaces HATEOAS.
            .collect(Collectors.toList());

    // Si la lista está vacía, retorna código 204 (sin contenido).
    if (tipoProductos.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    // Devuelve la colección de tipos de productos con enlace "self".
    return ResponseEntity.ok(
        CollectionModel.of(
            tipoProductos, // Lista con los tipos de productos.
            linkTo(methodOn(TipoProductoControllerV2.class).GetAllTipoProductos()).withSelfRel() // Enlace a sí mismo.
        )
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 200 OK cuando se encuentra el tipo de producto.
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de producto encontrado exitosamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = TipoProductoModel.class)
            )
        ),
        // Respuesta 404 Not Found cuando no se encuentra el tipo de producto.
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de producto no encontrado.",
            content = @Content
        )
    })

        // Endpoint GET que devuelve un tipo de producto con HATEOAS por su ID.
        @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<TipoProductoModel>> GetById(@PathVariable Long id) {                                   // GET BY ID.
            return Optional.ofNullable(tipoProductoService.obtenerTipoProductoPorId(id)) // Llama al servicio y permite null.
            .map(tipoProductoModelAssembler::toModel) // Si lo encuentra, lo transforma con enlaces HATEOAS.
            .map(ResponseEntity::ok) // Devuelve 200 OK si existe.
            .orElse(ResponseEntity.notFound().build()); // Si no existe, devuelve 404 Not Found.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Anotación para Swagger que documenta el propósito del endpoint.
    @Operation(
        summary = "Crear un tipo de producto (HATEOAS)", // Título breve para Swagger UI.
        description = "Crea un nuevo tipo de producto en el sistema y devuelve el recurso con enlaces HATEOAS." // Descripción extendida.
    )

    // Documenta las posibles respuestas de este endpoint.
    @ApiResponses(value = {
    // Respuesta 201 Created cuando se crea correctamente.
    @ApiResponse(
        responseCode = "201",
        description = "Tipo de producto creado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con HATEOAS.
            schema = @Schema(implementation = TipoProductoModel.class) // Modelo que se devolverá.
        )
    ),
    // Respuesta 400 Bad Request cuando los datos enviados son inválidos.
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud incorrecta. Los datos del tipo de producto no son válidos.",
        content = @Content // Sin cuerpo definido.
    )
})

    // Define que este método maneja POST y devuelve un EntityModel con media type HATEOAS.
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoProductoModel>> CrearTipoProducto(@RequestBody TipoProductoModel tipoProducto) {                          // POST.
    // Guarda el nuevo tipo de producto en la base de datos.
    TipoProductoModel nuevoTipoProducto = tipoProductoService.guardarTipoProducto(tipoProducto);

    // Lo transforma en un EntityModel con enlaces HATEOAS usando el assembler.
    EntityModel<TipoProductoModel> entityModel = tipoProductoModelAssembler.toModel(nuevoTipoProducto); 

    // Retorna una respuesta 201 Created con:
    // - Location apuntando al enlace "self" del recurso creado
    // - El cuerpo del tipo de producto con enlaces HATEOAS
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // Header Location.
            .body(entityModel); // Cuerpo con HATEOAS.
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger para describir el propósito del endpoint PUT.
    @Operation(
        summary = "Actualizar un tipo de producto (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza completamente un tipo de producto por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Documenta las posibles respuestas para el endpoint.
    @ApiResponses(value = {
    // Respuesta exitosa.
    @ApiResponse(
        responseCode = "200",
        description = "Tipo de producto actualizado correctamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
            schema = @Schema(implementation = TipoProductoModel.class) // Modelo que documenta el contenido.
        )
    ),
    // Cuando el recurso no se encuentra.
    @ApiResponse(
        responseCode = "404",
        description = "Tipo de producto no encontrado.",
        content = @Content // Sin contenido definido para este caso.
    )
})

    // Define que este método maneja PUT y produce contenido HATEOAS.
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoProductoModel>> actualizarTipoProducto(                                                          // PUT.
        @PathVariable Long id, // ID del tipo de producto que se quiere actualizar.
        @RequestBody TipoProductoModel tipoProductoModel // Datos nuevos para reemplazar completamente al método actual.
    ) {
        try {
            // Llama al servicio para realizar la actualización.
            TipoProductoModel tipoProductoActualizado = tipoProductoService.actualizarTipoProducto(id, tipoProductoModel);

            // Transforma el resultado en EntityModel con enlaces HATEOAS.
            EntityModel<TipoProductoModel> entityModel = tipoProductoModelAssembler.toModel(tipoProductoActualizado);

            // Retorna HTTP 200 OK con el recurso actualizado y enlaces.
            return ResponseEntity.ok(entityModel);
        } catch (RuntimeException e) {
            // Si no encuentra el tipo de producto, devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documenta el endpoint PATCH para Swagger.
    @Operation(
        summary = "Editar parcialmente el tipo de producto (HATEOAS)", // Título visible en Swagger UI.
        description = "Actualiza parcialmente el tipo de producto por su ID y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )

    // Posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta exitosa.
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de producto actualizado correctamente con enlaces HATEOAS.",
            content = @Content(
                mediaType = "application/hal+json", // Formato HAL con enlaces HATEOAS.
                schema = @Schema(implementation = TipoProductoModel.class) // Modelo que representa el recurso.
            )
        ),
        // Respuesta cuando no se encuentra el tipo de producto.
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de producto no encontrado.",
            content = @Content // No se devuelve cuerpo específico.
        )
    })

    // Define que el método maneja PATCH y produce HAL+JSON.
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoProductoModel>> actualizaTipoProductoParcial(                                               // PATCH.
        @PathVariable Long id, // ID del tipo de producto a actualizar.
        @RequestBody TipoProductoModel tipoProductoDetalles // Datos nuevos (pueden ser parciales).
    ) {
        try {
            // Llama al servicio para actualizar parcialmente el recurso.
            TipoProductoModel tipoProductoActualizado = tipoProductoService.actualizarTipoProductoParcial(id, tipoProductoDetalles);

            // Lo transforma en un EntityModel con enlaces HATEOAS.
            EntityModel<TipoProductoModel> entityModel = tipoProductoModelAssembler.toModel(tipoProductoActualizado);

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
        summary = "Eliminar el tipo de producto (HATEOAS)", // Título visible en Swagger UI.
        description = "Elimina el tipo de producto por su ID." // Explicación detallada.
    )

    // Documenta las posibles respuestas del endpoint.
    @ApiResponses(value = {
        // Respuesta 204 No Content cuando se elimina correctamente.
        @ApiResponse(
            responseCode = "204",
            description = "Tipo de producto eliminado exitosamente."
        ),
        // Respuesta 404 Not Found si no se encuentra el recurso.
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de producto no encontrado."
        )
    })

    // Define que este método maneja DELETE, aunque no produce contenido HATEOAS.
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> EliminarTipoProducto(@PathVariable Long id) {                                                        // DELETE.
        try {
            // Intenta eliminar el tipo de producto usando el servicio.
            tipoProductoService.eliminarTipoProducto(id);

            // Si se elimina exitosamente, devuelve 204 No Content.
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Si hay una excepción (como que no exista), devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
