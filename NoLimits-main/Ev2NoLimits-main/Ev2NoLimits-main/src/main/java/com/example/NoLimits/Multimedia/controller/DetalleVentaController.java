package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.DetallePedidoModel;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.service.DetalleVentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/api/v1/detalle_ventas","/api/detalleVentas"})
@Tag(name = "DetalleVenta-Controller", description = "Operaciones relacionadas con los detalles de las ventas.")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para obtener todos los detalles de venta
    @GetMapping
    // Documenta y describe en Swagger UI para Listar DetalleVenta.
    @Operation(summary = "Obtener todos los detalles de las ventas.", description = "Obtiene una lista de todos los detalles de las ventas.")
    @ApiResponse(
        responseCode = "200",
        description = "Detalles de ventas encontrados.",
            // @Content: Especifica el tipo de contenido de la solicitud o respuesta de la API.
            // mediaType = "application/json": Indica que el contenido está en formato JSON.
            content = @Content(mediaType = "application/json",
            // Indica que la estructura del JSON se basa en la clase DetalleVentaModel.
                schema = @Schema(implementation = DetalleVentaModel.class)
    // ---------------------------------------------------------------------------------------------
    // Swagger usará esta clase para generar automáticamente la documentación del modelo de datos.                              GET ALL.
    // ---------------------------------------------------------------------------------------------
    )
)
    public ResponseEntity<List<DetalleVentaModel>> listarDetallesVenta() {
        List<DetalleVentaModel> detalles = detalleVentaService.findAll();
        if (detalles.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 si no hay contenido
        }
        return ResponseEntity.ok(detalles);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para obtener un detalle de venta por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar detalle de la venta.", description = "Obtiene un detalle de una venta específica por su ID.")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Detalle de venta encontrado exitosamente.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DetalleVentaModel.class)
                )
            ),
            @ApiResponse(                                                                                                   // GET BY ID.
                responseCode = "404",
                description = "Detalle de venta no encontrado."
            )
        })
    public ResponseEntity<DetalleVentaModel> buscarPorId(@PathVariable Long id) {
        Optional<DetalleVentaModel> detalleVenta = detalleVentaService.findById(id);
        // Si el detalle de venta existe, lo devuelve con un 200 OK. Si no, devuelve 404 Not Found.
        return detalleVenta.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para crear un nuevo detalle de venta
    @PostMapping
    @Operation(summary = "Crear los detalles de la venta.", description = "Crea un nuevo detalle de una venta en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Detalle de venta creado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DetalleVentaModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",                                                                                           // POST BY ID.
            description = "Solicitud incorrecta. Los datos del detalle de la venta no son válidos."
        )
    })
    public ResponseEntity<DetalleVentaModel> crearDetalleVenta(@RequestBody DetalleVentaModel detalleVenta) {
        DetalleVentaModel nuevoDetalle = detalleVentaService.save(detalleVenta);
        // Devuelve el nuevo recurso creado con el estado 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para eliminar un detalle de venta
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar el detalle de una venta.", description = "Elimina el detalle de una venta por su ID.")
    @ApiResponses(value = {@ApiResponse(
                responseCode = "204",
                description = "Detalle de venta eliminado exitosamente."
            ),
        @ApiResponse(
            responseCode = "404",
            description = "Detalle de venta no encontrado."                                                                        // DELETE BY ID.
        )
    })
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        // Primero verifica si existe
        if (!detalleVentaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, devuelve 404
        }
        detalleVentaService.deleteById(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 si se eliminó con éxito
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para actualizar completamente un detalle de venta
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un detalle de una venta.", description = "Actualiza completamente el detalle de una venta por su ID.")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Detalle de venta actualizado correctamente.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccesorioModel.class)
                )
            ),
        @ApiResponse(                                                                                                        // PUT BY ID.
                responseCode = "404",
                description = "Detalle de venta no encontrado."                                                                     
            )
        })
    public ResponseEntity<DetalleVentaModel> actualizarDetalleVenta(@PathVariable Long id, @RequestBody DetalleVentaModel detalleVenta) {
        DetalleVentaModel detalleActualizado = detalleVentaService.update(id, detalleVenta);
        if (detalleActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalleActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para actualizar parcialmente un detalle de venta
    @PatchMapping("/{id}")
    @Operation(summary = "Editar parcialmente el detalle de una venta.", description = "Actualiza parcialmente el detalle de una venta por su ID.")
        @ApiResponses(value = {
    @ApiResponse(
            responseCode = "200",
            description = "Detalle de venta actualizado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccesorioModel.class)
            )
        ),                                                                                                                    // PATCH BY ID.
    @ApiResponse(
            responseCode = "404",
            description = "Detalle de venta no encontrado."
        )
    })
    public ResponseEntity<DetalleVentaModel> editarDetalleVenta(@PathVariable Long id, @RequestBody DetalleVentaModel detalleVenta) {
        DetalleVentaModel detalleActualizado = detalleVentaService.patchDetalleVentaModel(id, detalleVenta);
        if (detalleActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalleActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapea un resumen.
    @GetMapping({"/resumen"})
    @Operation(
        summary = "Resumen de detalles de venta.",
        description = "Obtiene un resumen con datos clave de los detalles de venta, incluyendo información relevante."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de detalles de venta obtenido exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(
                        type = "object",
                        description = "Mapa con atributos resumidos del detalle de venta (por ejemplo: ID, cantidad, accesorio, método de pago)."
                    )
                )
            )                                                                                                                // GET BY RESUMEN.
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No hay datos de resumen disponibles."
        )
    })
    // Map: Estructura de datos.
    // Object: Objeto creado que almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenDetalleVenta(){
        // Devuelve el método resumenDetalleVenta para resumen.
        List<Map<String, Object>> resumenDetalleVenta = detalleVentaService.obtenerDetalleVentaConDatos();
        if(resumenDetalleVenta.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenDetalleVenta);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
