package com.example.NoLimits.Multimedia.controller;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.service.MetodoPagoService;

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
@RequestMapping("/api/v1/metodoPagos")
@Tag(name = "MétodoPago-Controller.", description = "Operaciones relacionadas con los método de pagos.")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtiene todos los metodos de pago
    @GetMapping
    @Operation(summary = "obtener todos los métodos de pago.", description = "Obtiene una lista de todos los métodos de pago.")
    @ApiResponse(
        responseCode = "200",
        description = "Método de pago encontrado",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = MetodoPagoModel.class)
        )
    )                                                                                                                               // GET ALL.
    public ResponseEntity<List<MetodoPagoModel>> getAllMetodosPago() { // Obtiene todos los metodos de pago.
        List<MetodoPagoModel> metodos = metodoPagoService.findAll(); // Busca todos los metodos de pago.
        if (metodos.isEmpty()) { // Si la lista de metodos de pago esta vacia, retorna un codigo 204 (No Content).
            return ResponseEntity.noContent().build(); // Retorna un codigo 204 (No Content).
        }
        return ResponseEntity.ok(metodos); // Retorna un codigo 200 (OK) con la lista de metodos de pago.
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Busca un metodo de pago por su ID
    @GetMapping("/{id}")
    // Busca un metodo de pago por su ID
    @Operation(summary = "Buscar método de pago por ID", 
    description = "Obtiene un método de pago específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
        description = "Método de pago encontrado exitosamente", 
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = MetodoPagoModel.class))),
        @ApiResponse(responseCode = "404",                                                                                          // GET BY ID.
        description = "Método de pago no encontrado")
    })
    public ResponseEntity<MetodoPagoModel> getMetodoPagoById(@PathVariable Long id) { // Busca un metodo de pago por su ID.
        return metodoPagoService.findById(id) // Busca un metodo de pago por su ID 
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Si el metodo de pago no se encuentra, retorna un codigo 404 (Not Found).
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // crea un nuevo metodo de pago
    @PostMapping
    // crea un nuevo metodo de pago
    @Operation(summary = "Crear un método de pago.", 
    description = "Crea un nuevo método de pago en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
        description = "Método de pago creado correctamente", 
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = MetodoPagoModel.class))),                                                         // POST.
        @ApiResponse(responseCode = "400", 
        description = "Solicitud incorrecta. Los datos del método de pago no son válidos")
    })
    public ResponseEntity<MetodoPagoModel> CrearMetodoPago(@RequestBody MetodoPagoModel metodoPago) { // Crea un nuevo metodo de pago.
        MetodoPagoModel nuevoMetodo = metodoPagoService.save(metodoPago); // Guarda el nuevo metodo de pago.
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMetodo); // Retorna un codigo 201 (Created) con el nuevo metodo de pago.
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualiza un metodo de pago existente
    @PutMapping("/{id}")
    // Actualiza un metodo de pago existente
    @Operation(summary = "Actualizar un método de pago", 
    description = "Actualiza completamente un método de pago por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
        description = "Método de pago actualizado correctamente", 
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = MetodoPagoModel.class))),                                                         // PUT.
        @ApiResponse(responseCode = "404", 
        description = "Método de pago no encontrado")
    })
    public ResponseEntity<MetodoPagoModel> ActualizaMetodoPago(@PathVariable Long id, @RequestBody MetodoPagoModel metodoPagoDetails) { // Guarda un nuevo metodo de pago.
        try {
            MetodoPagoModel actualizametodo = metodoPagoService.update(id, metodoPagoDetails); // Actualiza el metodo de pago con los detalles proporcionados.
            return ResponseEntity.ok(actualizametodo); // Retorna un codigo 200 (OK) con el metodo de pago actualizado.
        } catch (RuntimeException e) { // Captura la excepcion si el metodo de pago no se encuentra.
            return ResponseEntity.notFound().build(); // Retorna un codigo 404 (Not Found) si el metodo de pago no se encuentra.
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualiza parcialmente un metodo de pago existente
    @PatchMapping("/{id}")
    // Actualiza parcialmente un metodo de pago existente
    @Operation(summary = "Editar parcialmente un método de pago", 
    description = "Actualiza parcialmente un método de pago por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
        description = "Método de pago actualizado correctamente", 
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = MetodoPagoModel.class))),
        @ApiResponse(responseCode = "404",                                                                                  // PATCH.
        description = "Método de pago no encontrado")
    })
    public ResponseEntity<MetodoPagoModel> patchMetodoPago(@PathVariable Long id, @RequestBody MetodoPagoModel metodoPagoDetails) { // Actualiza parcialmente un metodo de pago existente.
        try {
            MetodoPagoModel patchedMetodo = metodoPagoService.patch(id, metodoPagoDetails); // Actualiza parcialmente el metodo de pago con los detalles proporcionados.
            return ResponseEntity.ok(patchedMetodo); // Retorna un codigo 200 (OK) con el metodo de pago actualizado.
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si el metodo de pago no se encuentra, retorna un codigo 404 (Not Found).
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Elimina un metodo de pago por su ID
    @DeleteMapping("/{id}")
    // Elimina un metodo de pago por su ID
    @Operation(summary = "Eliminar un método de pago.", 
    description = "Elimina un método de pago por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Método de pago eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado")
    })                                                                                                                                      // DELETE.
    public ResponseEntity<Void> EliminarMetodoPago(@PathVariable Long id) { // Elimina un metodo de pago por su ID.
        if (!metodoPagoService.findById(id).isPresent()) { // Verifica si el metodo de pago existe.
            return ResponseEntity.notFound().build(); // Si el metodo de pago no se encuentra, retorna un codigo 404 (Not Found).
        }
        metodoPagoService.deleteById(id); // Elimina el metodo de pago por su ID.
        return ResponseEntity.noContent().build(); // Retorna un codigo 204 (No Content) si la eliminacion fue exitosa.
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Busca un metodo de pago por su nombre
    @GetMapping("/nombre/{nombre}")
    @Operation(
        summary = "Buscar método de pago por nombre.",
        description = "Obtiene un método de pago específico según el nombre proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Método de pago encontrado exitosamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MetodoPagoModel.class)
            )                                                                                                           // GET BY NOMBRE.
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Método de pago no encontrado."
        )
    })
    public ResponseEntity<MetodoPagoModel> getMetodoPagoByNombre(@PathVariable String nombre) {
        Optional<MetodoPagoModel> metodoPago = metodoPagoService.findByNombre(nombre);
        // Si se encuentra, devuelve 200 OK con el objeto. Si no, devuelve 404 Not Found.
        return metodoPago.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapea un resumen.
    @GetMapping({"/resumen"})
    @Operation(
        summary = "Resumen de métodos de pago.",
        description = "Obtiene un resumen con datos clave de los métodos de pago registrados en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de métodos de pago obtenido exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(
                        type = "object",
                        description = "Mapa con atributos resumidos del método de pago (por ejemplo: ID, nombre, tipo)."
                    )
                )
            )                                                                                                                   // GET BY RESUMEN.
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No hay datos de resumen disponibles."
        )
    })
    // Map: Estructura de datos.
    // Object: Objeto creado que almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenMetodoPagos(){
        // Devuelve el método obtenerMetodoPagoConDatos para resumen.
        List<Map<String, Object>> resumenMetodoPagos = metodoPagoService.obtenerMetodoPagoConDatos();
        if(resumenMetodoPagos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenMetodoPagos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}