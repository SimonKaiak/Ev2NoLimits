package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.TipoProductoModel;
import com.example.NoLimits.Multimedia.service.TipoProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// ´Para el uso de APIs RESTful
@RestController
// Dirección.
@RequestMapping({"api/v1/tipo_productos", "api/tipoProductos"})
// Permite a esta clase validar valores null.
@Validated
@Tag(name = "TipoProducto-Controller", description = "Operaciones relacionadas con los tipos de productos.")
// Clase TipoProductoController.
public class TipoProductoController {

    // Aplica herencia de TipoProductoService.
    @Autowired
    private TipoProductoService tipoProductoService;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtiene el mapeo.
    @GetMapping    
    // Documenta y describe en Swagger UI para Listar TipoProducto.
    @Operation(summary = "Obtener todos los tipos de productos.", description = "Obtiene una lista de todos los tipos de productos.")
    @ApiResponse(
        responseCode = "200",
        description = "Tipos de productos encontrados.",
            // @Content: Especifica el tipo de contenido de la solicitud o respuesta de la API.
            // mediaType = "application/json": Indica que el contenido está en formato JSON.
            content = @Content(mediaType = "application/json",
            // Indica que la estructura del JSON se basa en la clase TipoProductoModel.
                schema = @Schema(implementation = TipoProductoModel.class)
    // ---------------------------------------------------------------------------------------------
    // Swagger usará esta clase para generar automáticamente la documentación del modelo de datos.                              GET ALL.
    // ---------------------------------------------------------------------------------------------
    )
)
    // Atributo como Entidad de AccesorioModel que contiene lista. Se llamará a través de "ListaTipoProducto".
    public ResponseEntity<List<TipoProductoModel>> ListaTipoProductos(){
        // Se obtienen tipos de producto mediante el método "obtenerTiposProducto" desde TipoProductoService.
        List<TipoProductoModel> tiposProductos = tipoProductoService.obtenerTiposProductos();
        // Si tipoProducto está vacío:.
        if(tiposProductos.isEmpty()){
            // Deuvelve error.
            return ResponseEntity.noContent().build();
        }
        // Sino devuelve okey.
        return ResponseEntity.ok(tiposProductos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Buscar por Nombre.
    @GetMapping("/nombre/{nombre}")
    @Operation(
        summary = "Buscar tipos de producto por nombre.",
        description = "Obtiene una lista de tipos de producto que coincidan con el nombre proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tipos de producto encontrados exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TipoProductoModel.class))
            )
        ),                                                                      
        @ApiResponse(
            responseCode = "204",
            description = "No se encontraron tipos de producto con ese nombre."                                                         // GET BY NOMBRE.
        )
    })
    // Atrinuto como Entidad de TipoProductoModel que contiene lista. Se llamará a través de "buscarPorNombre".
    // PathVariable: Captura valores de una URL.
    public ResponseEntity<List<TipoProductoModel>> buscarPorNombre(@PathVariable("nombre") String nombreTipoProducto){
        // Se obtienen tiposProductos mediante el método "obtenerTipoProductoPorNombre" desde tipoProductoService.
        List<TipoProductoModel> tiposProductos = tipoProductoService.obtenerTipoProductoPorNombre(nombreTipoProducto);
        // Si accesorios está vacío:.
        if (tiposProductos.isEmpty()){
            // Devuelve error.
            return ResponseEntity.noContent().build();
        }
        // Sino devuelve okey.
        return ResponseEntity.ok(tiposProductos);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapear por id
    @GetMapping({"/{id}"})
    @Operation(summary = "Buscar un tipo de producto.", description = "Obtiene un tipo de producto específico por su ID.")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Tipo de producto encontrado exitosamente.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TipoProductoModel.class)
                )
            ),
            @ApiResponse(                                                                                                   // GET BY ID.
                responseCode = "404",
                description = "Tipo de producto no encontrado."
            )
        })
    public ResponseEntity<TipoProductoModel> buscarPorId(@PathVariable("id") Long idTipoProducto) {
        // Es obtenido por método service.
        TipoProductoModel tipoProductoModel = tipoProductoService.obtenerTipoProductoPorId(idTipoProducto);
        // Devuelve error cuando es nullo cuando no exista, de lo contrario devuelve ok.
        return tipoProductoModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(tipoProductoModel);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Crea un objeto.
    @PostMapping
    @Operation(summary = "Crear los tipos de productos.", description = "Crea un nuevo tipo de producto en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Tipo de producto creado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TipoProductoModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",                                                                                           // POST BY ID.
            description = "Solicitud incorrecta. Los datos del tipo del producto no son válidos."
        )
    })
    // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.
    public ResponseEntity<TipoProductoModel> guardarTipoProducto(@RequestBody TipoProductoModel tipoProductoModel){
        // Devuelve el mpetodo guardarAccesorio para nuevoAccesorio.
        TipoProductoModel nuevoTipoProducto = tipoProductoService.guardarTipoProducto(tipoProductoModel);
        // Se creó exitosamente un tipo de producto.
        return ResponseEntity.status(201).body(nuevoTipoProducto);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualiza parcial.
    @PutMapping({"/{id}"})
    @Operation(summary = "Actualizar un tipo de producto.", description = "Actualiza completamente un tipo de producto por su ID.")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Tipo de producto actualizado correctamente.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccesorioModel.class)
                )
            ),
        @ApiResponse(                                                                                                        // PUT BY ID.
                responseCode = "404",
                description = "Tipo de producto no encontrado."                                                                     
            )
        })
        // Devuelve el método actualizarTipoProducto.
        // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.
        public ResponseEntity<TipoProductoModel> actualizarTipoProducto(@PathVariable("id") Long idTipoProducto, @RequestBody TipoProductoModel tipoProductoModel){
        TipoProductoModel tipoProductoActualizado = tipoProductoService.actualizarTipoProducto(idTipoProducto, tipoProductoModel);
        if(tipoProductoActualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tipoProductoActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Método para actualizar por id específicamente algo.
    @PatchMapping({"/{id}"})
    @Operation(summary = "Editar parcialmente un tipo de producto.", description = "Actualiza parcialmente un tipo de producto por su ID.")
        @ApiResponses(value = {
    @ApiResponse(
            responseCode = "200",
            description = "Tipo de producto actualizado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccesorioModel.class)
            )
        ),                                                                                                                    // PATCH BY ID.
    @ApiResponse(
            responseCode = "404",
            description = "Tipo de producto no encontrado."
        )
    })
    public ResponseEntity<TipoProductoModel> editarTipoProducto(@PathVariable("id") Long idTipoProducto, @RequestBody TipoProductoModel tipoProductoModel){
        TipoProductoModel tipoProductoActualizado = tipoProductoService.actualizarTipoProductoParcial(idTipoProducto, tipoProductoModel);
        return tipoProductoActualizado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(tipoProductoActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Método para eliminar por id.
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un tipo de producto.", description = "Elimina un tipo de producto por su ID.")
    @ApiResponses(value = {@ApiResponse(
                responseCode = "204",
                description = "Tipo de producto eliminado exitosamente."
            ),
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de producto no encontrado."                                                                        // DELETE BY ID.
        )
    })
    public ResponseEntity<Void> eliminarTipoProducto(@PathVariable("id") Long idTipoProducto){
        tipoProductoService.eliminarTipoProducto(idTipoProducto);
        return ResponseEntity.noContent().build();
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapea un resumen.
    @GetMapping({"/resumen"})
    @Operation(
        summary = "Resumen de tipos de producto.",
        description = "Obtiene un resumen con información clave de los tipos de producto asociados a los accesorios."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de tipos de producto obtenido exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(
                        type = "object",
                        description = "Mapa con atributos resumidos del tipo de producto (por ejemplo: ID, nombre del tipo)."
                    )
                )
            )
        ),                                                                                                                         // GET BY RESUMEN.
        @ApiResponse(
            responseCode = "204",
            description = "No hay datos de resumen disponibles."
        )
    })
    // Map: Estructura de datos.
    // Object: Objeto creado qhe almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenTipoProducto(){
        // Devuelve el método obtenerAccesorioConNombres para resumenAccesorio.
        List<Map<String, Object>> resumenTipoProducto = tipoProductoService.obtenerTipoProductoConNombres();
        if(resumenTipoProducto.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenTipoProducto);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}