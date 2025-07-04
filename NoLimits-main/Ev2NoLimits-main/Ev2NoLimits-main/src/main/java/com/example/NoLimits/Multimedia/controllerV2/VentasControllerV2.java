package com.example.NoLimits.Multimedia.controllerV2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.example.NoLimits.Multimedia.assemblers.VentasModelAssembler;
import com.example.NoLimits.Multimedia.model.VentasModel;
import com.example.NoLimits.Multimedia.service.VentasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping( // Para formato en Swagger.
    value = "/api/v2/ventas", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "Ventas-Controller-V2", description = "Operaciones relacionadas con las ventas.")
public class VentasControllerV2 {

    @Autowired
    private VentasService ventasService;

    @Autowired
    private VentasModelAssembler ventasassembler;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Listar todas las ventas
    @GetMapping(produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar todas las ventas.", description = "Obtiene una lista de todas las ventas registradas.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", 
        description = "Lista de ventas obtenida correctamente.",
        content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = VentasModel.class)))),
    @ApiResponse(responseCode = "204",
        description = "No se encontraron ventas registradas.")
    })
    public ResponseEntity<CollectionModel<EntityModel<VentasModel>>> getAllVentas() {                                                       // GET ALL.
    // Obtiene la lista de ventas desde el servicio
    List<EntityModel<VentasModel>> ventas = ventasService.findAll().stream()
            .map(ventasassembler::toModel) // Transforma cada venta a su EntityModel con enlaces HATEOAS
            .collect(Collectors.toList());

    if (ventas.isEmpty()) {
        return ResponseEntity.noContent().build(); // Retorna 204 si no hay datos
    }

    // Retorna 200 con el listado y el enlace HATEOAS al endpoint mismo
    return ResponseEntity.ok(CollectionModel.of(
        ventas,
        linkTo(methodOn(VentasControllerV2.class).getAllVentas()).withSelfRel()
    ));
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener una venta por su ID
    @GetMapping(value = "/{id}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Buscar venta por ID.", description = "Obtiene una venta específica por su ID.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", 
        description = "Venta encontrada correctamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = VentasModel.class))),
    @ApiResponse(responseCode = "404", 
        description = "Venta no encontrada.")
    })
    public ResponseEntity<EntityModel<VentasModel>> getVentaById(@PathVariable Long id) {                                               // GET BY ID.
    try {
        VentasModel venta = ventasService.findById(id);
        EntityModel<VentasModel> entityModel = ventasassembler.toModel(venta);
        return ResponseEntity.ok(entityModel); // Devuelve 200 con la venta y sus links HATEOAS
    } catch (Exception e) {
        return ResponseEntity.notFound().build(); // Devuelve 404 si no se encuentra la venta
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener ventas por método de pago (V2 con HATEOAS)
    @GetMapping(value = "/metodopago/{metodoPagoId}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Buscar ventas por método de pago.",
           description = "Obtiene todas las ventas realizadas usando un método de pago específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                 description = "Ventas encontradas exitosamente.",
                 content = @Content(mediaType = "application/json",
                 array = @ArraySchema(schema = @Schema(implementation = VentasModel.class)))),
    @ApiResponse(responseCode = "404",
                 description = "No se encontraron ventas con ese método de pago.")
})
    public CollectionModel<EntityModel<VentasModel>> getVentaByMetodoPago(@PathVariable Long metodoPagoId) {                        // GET VENTA BY METODO PAGO.
    
    // Obtiene la lista de ventas filtradas por ID de método de pago
    List<VentasModel> ventas = ventasService.obtenerVentaPorMetodoPago(metodoPagoId);

    // Si la lista está vacía, lanza error 404
    if (ventas.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron ventas con ese método de pago.");
    }

    // Transforma cada venta en un EntityModel con enlaces HATEOAS
    List<EntityModel<VentasModel>> ventasConLinks = ventas.stream()
            .map(ventasassembler::toModel)
            .collect(Collectors.toList());

    // Devuelve un CollectionModel con la lista de ventas y enlace a sí mismo
    return CollectionModel.of(ventasConLinks,
            linkTo(methodOn(VentasControllerV2.class).getVentaByMetodoPago(metodoPagoId)).withSelfRel());
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener ventas por usuario (V2 con HATEOAS)
    @GetMapping(value = "/usuario/{usuarioId}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Buscar ventas por usuario.",
           description = "Obtiene todas las ventas realizadas por un usuario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                 description = "Ventas encontradas exitosamente.",
                 content = @Content(mediaType = "application/json",
                 array = @ArraySchema(schema = @Schema(implementation = VentasModel.class)))),
    @ApiResponse(responseCode = "404",
                 description = "No se encontraron ventas asociadas al usuario.")
})
    public CollectionModel<EntityModel<VentasModel>> getVentaByUsuario(@PathVariable Long usuarioId) {                              // GET BY VENTA USUARIO.

    // Obtiene todas las ventas asociadas al usuario
    List<VentasModel> ventas = ventasService.obtenerVentaPorUsuario(usuarioId);

    // Si no hay resultados, se lanza una excepción 404
    if (ventas.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron ventas para este usuario.");
    }

    // Convierte la lista en EntityModel con enlaces HATEOAS
    List<EntityModel<VentasModel>> ventasConLinks = ventas.stream()
            .map(ventasassembler::toModel)
            .collect(Collectors.toList());

    // Devuelve un CollectionModel con los enlaces
    return CollectionModel.of(ventasConLinks,
            linkTo(methodOn(VentasControllerV2.class).getVentaByUsuario(usuarioId)).withSelfRel());
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Crear una nueva venta
    @PostMapping(produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear una nueva venta.", description = "Registra una nueva venta en el sistema.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "201", 
        description = "Venta creada correctamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = VentasModel.class))),
    @ApiResponse(responseCode = "400", 
        description = "Solicitud inválida. Verifica los datos enviados.")
})
    public ResponseEntity<EntityModel<VentasModel>> createVenta(@RequestBody VentasModel venta) {                                           // POST.
    VentasModel createdVenta = ventasService.save(venta); // Guarda la nueva venta
    EntityModel<VentasModel> entityModel = ventasassembler.toModel(createdVenta); // Ensambla con HATEOAS

    // Devuelve 201 Created con el Location apuntando al recurso creado
    return ResponseEntity
            .created(linkTo(methodOn(VentasControllerV2.class).getVentaById(createdVenta.getId())).toUri())
            .body(entityModel);
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualizar una venta por su ID
    @PutMapping(value = "/{id}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar una venta.", description = "Actualiza los datos de una venta existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta actualizada correctamente."),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada.")
})
    public ResponseEntity<EntityModel<VentasModel>> updateVenta(@PathVariable Long id, @RequestBody VentasModel venta) {                      // PUT.
    
    // Busca la venta existente por su ID
    VentasModel ventaExistente = ventasService.findById(id);
    
    // Verifica y actualiza los campos que no sean nulos
    if (venta.getFechaCompra() != null) {
        ventaExistente.setFechaCompra(venta.getFechaCompra());
    }
    if (venta.getHoraCompra() != null) {
        ventaExistente.setHoraCompra(venta.getHoraCompra());
    }
    if (venta.getTotalVenta() != null) {
        ventaExistente.setTotalVenta(venta.getTotalVenta());
    }

    // Guarda la venta actualizada
    VentasModel ventaActualizada = ventasService.save(ventaExistente);

    // Retorna la venta con el modelo HATEOAS y link a sí misma
    return ResponseEntity
            .created(linkTo(methodOn(VentasControllerV2.class).getVentaById(ventaActualizada.getId())).toUri())
            .body(ventasassembler.toModel(ventaActualizada));
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Editar parcialmente una venta existente
    @PatchMapping(value = "/{id}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Editar una venta.", description = "Edita los datos de una venta existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta editada correctamente."),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada.")
})
    public ResponseEntity<EntityModel<VentasModel>> patchVenta(@PathVariable Long id, @RequestBody VentasModel venta) {                    // PATCH.
    try {
        // Busca la venta existente
        VentasModel ventaExistente = ventasService.findById(id);

        // Aplica solo los campos que no son nulos
        if (venta.getFechaCompra() != null) {
            ventaExistente.setFechaCompra(venta.getFechaCompra());
        }
        if (venta.getHoraCompra() != null) {
            ventaExistente.setHoraCompra(venta.getHoraCompra());
        }
        if (venta.getTotalVenta() != null) {
            ventaExistente.setTotalVenta(venta.getTotalVenta());
        }

        // Guarda los cambios
        VentasModel ventaActualizada = ventasService.save(ventaExistente);

        // Devuelve con HATEOAS
        return ResponseEntity.ok(ventasassembler.toModel(ventaActualizada));

    } catch (Exception e) {
        // Si no se encuentra la venta, devuelve 404
        return ResponseEntity.notFound().build();
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Eliminar una venta por su ID
    @DeleteMapping(value = "/{id}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Eliminar una venta.", description = "Elimina una venta existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Venta eliminada correctamente."),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada.")
})
    public ResponseEntity<?> deleteVenta(@PathVariable Long id) {                                                           // DELETE.
    try {
        ventasService.deleteById(id);  // Intenta eliminar la venta por su ID
        return ResponseEntity.noContent().build(); // Éxito: 204 No Content
    } catch (Exception e) {
        return ResponseEntity.notFound().build(); // Error: 404 Not Found
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
}