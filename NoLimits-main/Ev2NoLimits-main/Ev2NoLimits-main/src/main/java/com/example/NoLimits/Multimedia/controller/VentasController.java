package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.NoLimits.Multimedia.model.VentasModel;
import com.example.NoLimits.Multimedia.service.VentasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/api/v1/ventas")
@Tag(name = "Ventas-Controller.", description = "Operaciones relacionadas con las ventas.")
public class VentasController {

    @Autowired
    private VentasService ventasService;

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping
    @Operation(summary = "Listar todas las ventas.", description = "Obtiene una lista de todas las ventas registradas.")
    // Anotación para documentar la operación de listar todas las ventas.
    @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida correctamente.", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = VentasModel.class)))
    @ApiResponse(responseCode = "204", description = "No se encontraron ventas registradas.", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = VentasModel.class)))
    public ResponseEntity<List<VentasModel>> listarVentas() {
        List<VentasModel> ventas = ventasService.findAll();
        if (ventas.isEmpty()) { // GET ALL.
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Buscar venta por ID.", description = "Obtiene una venta específica por su ID.")
    @ApiResponse(responseCode = "200", description = "Venta encontrada correctamente.", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = VentasModel.class)))
    @ApiResponse(responseCode = "404", description = "Venta no encontrada.", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = VentasModel.class)))
    public ResponseEntity<VentasModel> buscarPorId(@PathVariable Long id) {
        VentasModel venta = ventasService.findById(id);
        return ResponseEntity.ok(venta);
    }
    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PostMapping
    @Operation(summary = "Crear una nueva venta.", description = "Registra una nueva venta en el sistema.")
    @ApiResponse(responseCode = "201", description = "Venta creada correctamente.", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = VentasModel.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida. Verifica los datos enviados.", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = VentasModel.class)))
    public ResponseEntity<VentasModel> crear(@RequestBody VentasModel ventas) {
        VentasModel nuevaVenta = ventasService.save(ventas);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta); // POST.
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una venta.", description = "Actualiza los datos de una venta existente.")
    @ApiResponse(responseCode = "200", description = "Venta actualizada correctamente.")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada.")
    public ResponseEntity<VentasModel> actulizar(@PathVariable Long id, @RequestBody VentasModel ventas) {
        try {
            VentasModel ventasActualizado = ventasService.patchVentasModel(id, ventas);
            return ResponseEntity.ok(ventasActualizado); // PUT.
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PatchMapping("/{id}")
    @Operation(summary = "Editar una venta.", description = "Edita los datos de una venta existente.")
    @ApiResponse(responseCode = "200", description = "Venta editada correctamente.")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada.")
    public ResponseEntity<VentasModel> editarVenta(@PathVariable Long id, @RequestBody VentasModel venta) {
        try {
            VentasModel ventaActualizado = ventasService.patchVentasModel(id, venta);
            return ResponseEntity.ok(ventaActualizado); // PATCH.
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una venta.", description = "Elimina una venta existente por su ID.")
    // Anotación para Swagger, que permite documentar la API.
    @ApiResponse(responseCode = "204", description = "Venta eliminada correctamente.")
    // Anotación para documentar la operación de eliminar una venta.
    @ApiResponse(responseCode = "404", description = "Venta no encontrada.")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        ventasService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener ventas por método de pago
    @GetMapping("/metodopago/{metodoPagoId}")
    @Operation(summary = "Buscar ventas por método de pago.", description = "Obtiene todas las ventas realizadas usando un método de pago específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas encontradas exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VentasModel.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron ventas con ese método de pago.")
    })
    public ResponseEntity<List<VentasModel>> getVentaByMetodoPago(@PathVariable Long metodoPagoId) { // GET VENTA BY ID
                                                                                                     // METODO PAGO.
        // Llama al servicio para obtener las ventas asociadas a un método de pago
        List<VentasModel> ventas = ventasService.obtenerVentaPorMetodoPago(metodoPagoId);

        // Si no se encontraron ventas, retorna 404
        if (ventas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Si se encontraron, retorna la lista con 200 OK
        return ResponseEntity.ok(ventas);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapea un resumen.
    @GetMapping({ "/resumen" })
    @Operation(summary = "Resumen de ventas.", description = "Obtiene un resumen con información clave de las ventas, como ID, fecha de compra, total, método de pago, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen de ventas obtenido exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "object", description = "Mapa con atributos resumidos de la venta (por ejemplo: ID, fechaCompra, total, metodoPago)."))) // GET
                                                                                                                                                                                                                                                                                                                                  // BY
                                                                                                                                                                                                                                                                                                                                  // RESUMEN.
            ),
            @ApiResponse(responseCode = "204", description = "No hay datos de resumen disponibles.")
    })
    // Map: Estructura de datos.
    // Object: Objeto creado que almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenVentas() {
        // Devuelve el método resumenVentas para resumen.
        List<Map<String, Object>> resumenVentas = ventasService.obtenerVentasConDatos();
        if (resumenVentas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenVentas);
    }
    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
