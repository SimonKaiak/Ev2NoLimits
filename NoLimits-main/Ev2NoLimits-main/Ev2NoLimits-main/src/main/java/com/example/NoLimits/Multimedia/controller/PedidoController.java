package com.example.NoLimits.Multimedia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.model.PedidoModel;
import com.example.NoLimits.Multimedia.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pedidos")
@Validated
@Tag(name = "Pedido-Controller.", description = "Operaciones relacionadas con los pedidos.")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener todos los pedidos
    @GetMapping
    // Obtener todos los pedidos
    @Operation(summary = "obtener todos los pedidos.", 
    description = "Obtiene una lista de todos los pedidos.")
    @ApiResponse(
        responseCode = "200",
        description = "Pedido encontrado.",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PedidoModel.class)                                                                    // GET ALL.
        )
    )
    public ResponseEntity<List<PedidoModel>> listarPedidos() { // Método para listar todos los pedidos.
        List<PedidoModel> pedidos = pedidoService.ObtenerTodosLosPedidos(); // Llama al servicio para obtener todos los pedidos.
        if (pedidos.isEmpty()) { // Si la lista de pedidos está vacía, devuelve una respuesta sin contenido.
            return ResponseEntity.noContent().build(); // Si la lista de pedidos está vacía, devuelve una respuesta 204 No Content.
        }
        return ResponseEntity.ok(pedidos); // Si hay pedidos, devuelve una respuesta 200 OK con la lista de pedidos.
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener un pedido por ID
    @GetMapping("/{id}")
    // Obtener un pedido por ID
    @Operation(summary = "Buscar pedido por ID.", 
    description = "Obtiene un pedido específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
        description = "Pedido encontrado exitosamente",                                                                         // GET BY ID.
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = PedidoModel.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado.")
    })
    public ResponseEntity<PedidoModel> buscarPedidoPorId(@PathVariable Long id) { // Método para obtener un pedido por su ID.
        Optional<PedidoModel> pedido = pedidoService.ObtenerPedidoPorId(id); // Busca un pedido por su ID utilizando el servicio.
        return pedido.map(ResponseEntity::ok) // Si el pedido existe, devuelve una respuesta 200 OK con el pedido.
                     .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, devuelve una respuesta 404 Not Found.
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Crear un nuevo pedido
    @PostMapping
    // Crear un nuevo pedido
    @Operation(summary = "Crear un nuevo pedido.", 
    description = "Crea un nuevo pedido en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
        description = "Pedido creado correctamente.", 
        content = @Content(mediaType = "application/json",                                                                  // POST.
        schema = @Schema(implementation = PedidoModel.class))),
        @ApiResponse(responseCode = "400", 
        description = "Solicitud incorrecta. Los datos del pedido no son válidos.")
    })
    public ResponseEntity<PedidoModel> crearPedido(@Valid @RequestBody PedidoModel pedidoModel) { // Método para crear un nuevo pedido.
        PedidoModel nuevoPedido = pedidoService.guardarPedido(pedidoModel); // Guarda el nuevo pedido utilizando el servicio.
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido); // Devuelve el nuevo pedido con estado 201 Created.
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Buscar por Dirección pedido.
        @GetMapping("/direccionPedido/{direccionPedido}")
        @Operation(
            summary = "Buscar pedidos por dirección de entrega.",
            description = "Obtiene una lista de pedidos que coincidan con la dirección de entrega proporcionada."
        )
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Pedidos encontrados exitosamente.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PedidoModel.class))
                )
            ),
            @ApiResponse(
                responseCode = "204",                                                                                       // GET BY BUSCAR PEDIDO POR DIRECCIÓN DE ENTREGA.
                description = "No se encontraron pedidos con esa dirección."
            )
        })
        // Atrinuto como Entidad de PedidoModel que contiene lista. Se llamará a través de "buscarPedidoPorDireccionEntrega".
        // PathVariable: Captura valores de una URL.
        public ResponseEntity<List<PedidoModel>> buscarPedidoPorDireccionEntrega(@PathVariable("direccionPedido") String direccionEntrega){
        // Se obtienen pedidos mediante el método "obtenerPedidosPorDireccionEntrega" desde pedidoService.
        List <PedidoModel> pedidos = pedidoService.obtenerPedidoPorDireccionEntrega(direccionEntrega);
        // Si pedidos está vacío:.
        if (pedidos.isEmpty()){
            // Devuelve error.
            return ResponseEntity.noContent().build();
        }
        // Sino devuelve okey.
        return ResponseEntity.ok(pedidos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Buscar por estado.
        @GetMapping("/estado/{estado}")
        @Operation(
            summary = "Buscar pedidos por estado.",
            description = "Obtiene una lista de pedidos que coincidan con el estado proporcionado (por ejemplo: Pendiente, Enviado, Cancelado)."
        )
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Pedidos encontrados exitosamente.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PedidoModel.class))
                )
            ),
            @ApiResponse(
                responseCode = "204",                                                                                           // GET BY ESTADO.
                description = "No se encontraron pedidos con ese estado."
            )
        })
        // Atrinuto como Entidad de PedidoModel que contiene lista. Se llamará a través de "buscarPedidoPorEstado".
        // PathVariable: Captura valores de una URL.
    public ResponseEntity<List<PedidoModel>> buscarPedidoPorEstado(@PathVariable("estado") String estado){
        // Se obtienen pedidos mediante el método "obtenerPedidosPorEstado" desde pedidoService.
        List <PedidoModel> pedidos = pedidoService.obtenerPedidoPorEstado(estado);
        // Si pedidos está vacío:.
        if (pedidos.isEmpty()){
            // Devuelve error.
            return ResponseEntity.noContent().build();
        }
        // Sino devuelve okey.
        return ResponseEntity.ok(pedidos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualiza parcial.
    @PutMapping({"/{id}"})
    // Actualiza parcial.
    @Operation(summary = "Actualizar un pedido.", 
    description = "Actualiza completamente un pedido por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
        description = "Pedido actualizado correctamente.", 
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = PedidoModel.class))),
        @ApiResponse(responseCode = "404", 
        description = "Pedido no encontrado.")
    })
        // Devuelve el método actualizarTipoProducto.
        // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.                                                                            // PUT.
        // Crea método para actualizar Pedidos completos.
        public ResponseEntity<PedidoModel> actualizarPedido(@PathVariable("id") Long idPedido, @RequestBody PedidoModel pedidoModel){
        // Crea un objeto con los atributos y métodos de PedidoModel.
        PedidoModel pedidoActualizado = pedidoService.actualizarPedido(idPedido, pedidoModel);
        // Si no existe entonces:
        if(pedidoActualizado == null){
            // Error.
            return ResponseEntity.notFound().build();
        }
        // Actualiza
        return ResponseEntity.ok(pedidoActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualizar un pedido existente
    @PatchMapping("/{id}")
    // Actualizar un pedido existente
    @Operation(summary = "Editar parcialmente un pedido.", 
    description = "Actualiza parcialmente un pedido por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
        description = "Pedido actualizado correctamente.", 
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = PedidoModel.class))),
        @ApiResponse(responseCode = "404",                                                                                  // PATCH.
        description = "Pedido no encontrado.")
    })
    public ResponseEntity<PedidoModel> actualizarPedidoParcial(@PathVariable Long id, @RequestBody PedidoModel pedidoDetalles) {
        try {
            PedidoModel pedidoActualizado = pedidoService.ActualizarPedidoParcial(id, pedidoDetalles);
            return ResponseEntity.ok(pedidoActualizado); // Devuelve el pedido actualizado con estado 200 OK.
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si el pedido no se encuentra, devuelve una respuesta 404 Not Found.
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Eliminar un pedido
    @DeleteMapping("/{id}")
    // Eliminar un pedido
    @Operation(summary = "Eliminar un pedido.",
    description = "Elimina un pedido por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204",
        description = "Pedido eliminado exitosamente."),
        @ApiResponse(responseCode = "404",
        description = "Pedido no encontrado.")                                                                                      // DELETE.
    })
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) { // Metodo para elimnar un pedido por su ID
        try {
            pedidoService.eliminarPedido(id); // Llama al servicio para eliminar el pedido por su ID
            return ResponseEntity.noContent().build(); // Devuelve una respuesta 204 No Content si la eliminación es exitosa
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si el pedido no se encuentra, devuelve una respuesta 404 Not Found
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapea un resumen.
    @GetMapping({"/resumen"})   
    @Operation(
        summary = "Resumen de pedidos.",
        description = "Obtiene un resumen con datos clave de los pedidos, como ID, fecha, dirección y estado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de pedidos obtenido exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(
                        type = "object",
                        description = "Mapa con atributos resumidos del pedido (por ejemplo: ID, fechaPedido, estado, direcciónEntrega)."
                    )
                )
            )                                                                                                           // GET BY RESUMEN.
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No hay datos de resumen disponibles."
        )
    })
    // Map: Estructura de datos.
    // Object: Objeto creado que almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenPedidos(){
        // Devuelve el método obtenerPedidosConDatos para resumen.
        List<Map<String, Object>> resumenPedidos = pedidoService.obtenerPedidosConDatos();
        if(resumenPedidos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenPedidos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}