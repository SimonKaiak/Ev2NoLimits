package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.DetallePedidoModel;
import com.example.NoLimits.Multimedia.service.DetallePedidoService;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/api/v/detalle_pedidos","/api/detallePedidos"})
@Tag(name = "DetallePedido-Controller", description = "Operaciones relacionadas con los detalles del pedido.")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para obtener todos los detalles de pedido
    @GetMapping
    // Documenta y describe en Swagger UI para Listar DetallesPedido.
    @Operation(summary = "Obtener todos los detalles del pedido.", description = "Obtiene una lista de todos los detalles del pedido.")
    @ApiResponse(
        responseCode = "200",
        description = "Detalles de pedidos encontrados.",
            // @Content: Especifica el tipo de contenido de la solicitud o respuesta de la API.
            // mediaType = "application/json": Indica que el contenido está en formato JSON.
            content = @Content(mediaType = "application/json",
            // Indica que la estructura del JSON se basa en la clase DetallePedidoModel.
                schema = @Schema(implementation = DetallePedidoModel.class)
    // ---------------------------------------------------------------------------------------------
    // Swagger usará esta clase para generar automáticamente la documentación del modelo de datos.                              GET ALL.
    // ---------------------------------------------------------------------------------------------
    )
)
    public ResponseEntity<List<DetallePedidoModel>> listarDetallesPedido() {
        List<DetallePedidoModel> detalles = detallePedidoService.findAll();
        if (detalles.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content si no hay detalles de pedido
        }
        return ResponseEntity.ok(detalles); // Devuelve 200 OK con la lista de detalles de pedido
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para obtener un detalle de pedido por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar el detalle del pedido.", description = "Obtiene el detalle del pedido específico por su ID.")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Detalle del pedido encontrado exitosamente.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DetallePedidoModel.class)
                )
            ),
            @ApiResponse(                                                                                                   // GET BY ID.
                responseCode = "404",
                description = "Detalle del pedido no encontrado."
            )
        })
    public ResponseEntity<DetallePedidoModel> buscarPorId(@PathVariable Long id) {
        Optional<DetallePedidoModel> detallePedido = detallePedidoService.findById(id);
        return detallePedido.map(ResponseEntity::ok) // Si el detalle de pedido existe, devuelve 200 OK con el detalle
                            .orElseGet(() -> ResponseEntity.notFound().build()); // Devuelve 200 OK si el detalle de pedido existe, o 404 Not Found si no existe
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para crear un nuevo detalle de pedido
    @PostMapping
    @Operation(summary = "Crear los detalles del pedido.", description = "Crea un nuevo detalle de pedido en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Detalle del pedido creado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DetallePedidoModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",                                                                                           // POST BY ID.
            description = "Solicitud incorrecta. Los datos del detalle del pedido no son válidos."
        )
    })
    public ResponseEntity<DetallePedidoModel> crearDetallePedido(@RequestBody DetallePedidoModel detallePedido) {
        DetallePedidoModel nuevoDetalle = detallePedidoService.save(detallePedido); // Guarda el nuevo detalle de pedido utilizando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle); // Devuelve el nuevo detalle de pedido con estado 201 Created
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para eliminar un detalle de pedido
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar el detalle de un pedido.", description = "Elimina el detalle de un pedido por su ID.")
    @ApiResponses(value = {@ApiResponse(
                responseCode = "204",
                description = "Detalle de pedido eliminado exitosamente."
            ),
        @ApiResponse(
            responseCode = "404",
            description = "Detalle de pedido no encontrado."                                                                        // DELETE BY ID.
        )
    })
    public ResponseEntity<Void> eliminarDetallePedido(@PathVariable Long id) {
        if (!detallePedidoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si el detalle de pedido no existe, devuelve 404 Not Found
        }
        detallePedidoService.deleteById(id); // Elimina el detalle de pedido por ID
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content si la eliminación fue exitosa
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para actualizar completamente un detalle de pedido
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar el detalle de un pedido.", description = "Actualiza completamente el detalle de un pedido por su ID.")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Detalle de pedido actualizado correctamente.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DetallePedidoModel.class)
                )
            ),
        @ApiResponse(                                                                                                        // PUT BY ID.
                responseCode = "404",
                description = "Detalle de pedido no encontrado."                                                                     
            )
        })
    public ResponseEntity<DetallePedidoModel> actualizarDetallePedido(@PathVariable Long id, @RequestBody DetallePedidoModel detallePedido) {
        DetallePedidoModel detalleActualizado = detallePedidoService.update(id, detallePedido);
        if (detalleActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalleActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para actualizar parcialmente un detalle de pedido
    @PatchMapping("/{id}")
    @Operation(summary = "Editar parcialmente el detalle de un pedido.", description = "Actualiza parcialmente el detalle de un pedido por su ID.")
        @ApiResponses(value = {
    @ApiResponse(
            responseCode = "200",
            description = "Detalle del pedido actualizado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccesorioModel.class)
            )
        ),                                                                                                                    // PATCH BY ID.
    @ApiResponse(
            responseCode = "404",
            description = "Detalle del pedido no encontrado."
        )
    })
    public ResponseEntity<DetallePedidoModel> editarDetallePedido(@PathVariable Long id, @RequestBody DetallePedidoModel detallePedido) {
        DetallePedidoModel detalleActualizado = detallePedidoService.patch(id, detallePedido);
        if (detalleActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalleActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapea un resumen.
    @GetMapping({"/resumen"})
    @Operation(
        summary = "Resumen de detalles de pedido.",
        description = "Obtiene un resumen con datos clave de los detalles de pedido, incluyendo información relevante."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de detalles de pedido obtenido exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(
                        type = "object",
                        description = "Mapa con atributos resumidos del detalle del pedido (por ejemplo: ID, cantidad, accesorio)."
                    )
                )
            )                                                                                                               // GET BY RESUMEN.
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No hay datos de resumen disponibles."
        )
    })
    // Map: Estructura de datos.
    // Object: Objeto creado que almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenDetallePedido(){
        // Devuelve el método resumenDetallePedido para resumen.
        List<Map<String, Object>> resumenDetallePedido = detallePedidoService.obtenerDetallePedidoConDatos();
        if(resumenDetallePedido.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenDetallePedido);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
