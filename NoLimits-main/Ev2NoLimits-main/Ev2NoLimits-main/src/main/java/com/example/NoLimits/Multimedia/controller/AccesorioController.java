package com.example.NoLimits.Multimedia.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.service.AccesorioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
// Para el uso de APIs RESTful
@RestController
// Dirección.
@RequestMapping("api/v1/accesorios")
// Permite a esta clase validar valores null.
@Validated
// Tag para visualizar en Swagger UI.
@Tag(name = "Accesorios-Controller", description = "Operaciones relacionadas con los accesorios.")
// Clase AccesorioController.
public class AccesorioController {

    // Aplica herencia de AccesorioService.
    @Autowired
    // Clase AccesorioService.
    private AccesorioService accesorioService;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtiene el mapeo.
    @GetMapping  
    // Documenta y describe en Swagger UI para Listar Accesorios.
    @Operation(summary = "obtener todos los accesorios.", description = "Obtiene una lista de todos los accesorios.")
    @ApiResponse(
        responseCode = "200",
        description = "Accesorio encontrado",
            // @Content: Especifica el tipo de contenido de la solicitud o respuesta de la API.
            // mediaType = "application/json": Indica que el contenido está en formato JSON.
            content = @Content(mediaType = "application/json",
            // Indica que la estructura del JSON se basa en la clase AccesorioModel.
                schema = @Schema(implementation = AccesorioModel.class)
    // ---------------------------------------------------------------------------------------------
    // Swagger usará esta clase para generar automáticamente la documentación del modelo de datos.GET ALL.
    // ---------------------------------------------------------------------------------------------
    )
)
    // Atributo como Entidad de AccesorioModel que contiene lista. Se llamará a través de "ListaAccesorios".
    public ResponseEntity<List<AccesorioModel>> listarAccesorios(){
        // Se obtienen accesorios mediante el método "obtenerAccesorios" desde accesorioService.
        List<AccesorioModel> accesorios = accesorioService.obtenerAccesorios();
        // Si accesorios está vacío:.
        if(accesorios.isEmpty()){
            // Deuvelve error.
            return ResponseEntity.noContent().build();
        }
        // Sino devuelve okey.
        return ResponseEntity.ok(accesorios);
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Mapear por id
    @GetMapping({"/{id}"})
    @Operation(summary = "Buscar accesorio por ID", description = "Obtiene un accesorio específico por su ID")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Accesorio encontrado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccesorioModel.class)
                )
            ),
            @ApiResponse(                                                                                                   // GET BY ID.
                responseCode = "404",
                description = "Accesorio no encontrado"
            )
        })
        public ResponseEntity<AccesorioModel> buscarAccesorioPorId(@PathVariable("id") Long idAccesorio) {
            return accesorioService.obtenerAccesorioPorId(idAccesorio)
                .map(ResponseEntity::ok) // Si lo encuentra, lo devuelve con 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Actualiza parcial.
    @PutMapping({"/{id}"})
    @Operation(summary = "Actualizar un accesorio.", description = "Actualiza completamente un accesorio por su ID.")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Accesorio actualizado correctamente.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccesorioModel.class)
                )
            ),
        @ApiResponse(                                                                                                         // PUT BY ID.
                responseCode = "404",
                description = "Accesorio no encontrado."
            )
        })
        // Devuelve el método actualizarAccesorio.
        // RequestBody: Recibe datos HTTP como POST, PUT, PATCH.
        public ResponseEntity<AccesorioModel> actualizarAccesorioPorId(@PathVariable("id") Long idAccesorio, @RequestBody AccesorioModel accesorioModel){
        AccesorioModel accesorioActualizado = accesorioService.actualizarAccesorio(idAccesorio, accesorioModel);
        if(accesorioActualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accesorioActualizado);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Método para actualizar por id específicamente algo.
    @PatchMapping({"/{id}"})
    @Operation(summary = "Editar parcialmente un accesorio.", description = "Actualiza parcialmente un accesorio por su ID.")
        @ApiResponses(value = {
    @ApiResponse(
            responseCode = "200",
            description = "Accesorio actualizado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccesorioModel.class)
            )
        ),                                                                                                                     // PATCH BY ID.
    @ApiResponse(
            responseCode = "404",
            description = "Accesorio no encontrado."
        )
    })
    public ResponseEntity<AccesorioModel> editarAccesorioPorId(@PathVariable("id") Long id, @RequestBody AccesorioModel accesorioModel){
        AccesorioModel accesorioActualizado = accesorioService.actualizarAccesorioParcial(id, accesorioModel);
        return accesorioActualizado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(accesorioActualizado);
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PostMapping
    @Operation(summary = "Crear los accesorios.", description = "Crea un nuevo accesorio en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Accesorio creado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccesorioModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",                                                                                                 // POST BY ID.
            description = "Solicitud incorrecta. Los datos del accesorio no son válidos."
        )
    })
    // De la entidad Accesorio se crea un accesorio con sus atributos.
    // Validación automática, si tiene un error no lo procesa tampoco.
    public ResponseEntity<AccesorioModel> crearAccesorio(@Valid @RequestBody AccesorioModel accesorioModel) {
        // ResponseEntity.status(HttpStatus.CREATED): Devuélvele al cliente (Postman) una respuesta HTTP con código 201 CREATED
        // El cuerpo devuelve el accesorio guardado.
        return ResponseEntity.status(HttpStatus.CREATED).body(accesorioService.guardarAccesorio(accesorioModel));
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Método apra eliminar por id.
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un accesorio.", description = "Elimina un accesorio por su ID.")
    @ApiResponses(value = {@ApiResponse(
                responseCode = "204",
                description = "Accesorio eliminado exitosamente."
            ),
        @ApiResponse(
            responseCode = "404",
            description = "Accesorio no encontrado."                                                                             // DELETE BY ID.
        )
    })
    public ResponseEntity<Void> eliminarAccesorioPorId(@PathVariable("id") Long idAccesorio){
        accesorioService.eliminarAccesorioPorId(idAccesorio);
        return ResponseEntity.noContent().build();
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Buscar por Nombre.
        @GetMapping("/nombre/{nombre}")
        @Operation(
                summary = "Buscar accesorios por nombre.", 
                description = "Obtiene una lista de accesorios que coincidan con el nombre proporcionado."
            )
            @ApiResponses(value = {
                @ApiResponse(
                    responseCode = "200",
                    description = "Accesorios encontrados exitosamente.",
                    content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = AccesorioModel.class))
                    )
                ),
                @ApiResponse(
                    responseCode = "204",                                                                                       // GET BY NOMBRE.
                    description = "No se encontraron accesorios con ese nombre."
                )
            })
        // Atrinuto como Entidad de AccesorioModel que contiene lista. Se llamará a través de "buscarPorNombre".
        // PathVariable: Captura valores de una URL.
        public ResponseEntity<List<AccesorioModel>> buscarAccesorioPorNombre(@PathVariable("nombre") String nombreAccesorio){
        // Se obtienen accesorios mediante el método "obtenerAccesoriosPorNombre" desde accesorioService.
        List <AccesorioModel> accesorios = accesorioService.obtenerAccesoriosPorNombre(nombreAccesorio);
        // Si accesorios está vacío:.
        if (accesorios.isEmpty()){
            // Devuelve error.
            return ResponseEntity.noContent().build();
        }
        // Sino devuelve okey.
        return ResponseEntity.ok(accesorios);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtiene mapeo del tipo del Accesorio.
    @GetMapping("/tipo/{tipo}")  
        @Operation(
                summary = "Buscar accesorios por tipo.", 
                description = "Obtiene una lista de accesorios según el tipo especificado."
            )
            @ApiResponses(value = {
                @ApiResponse(
                    responseCode = "200",
                    description = "Accesorios encontrados exitosamente.",
                    content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = AccesorioModel.class))
                    )
                ),
                @ApiResponse(                                                                                                   // GET BY TIPO.
                    responseCode = "204",
                    description = "No se encontraron accesorios con ese tipo."
                )
            })
    // Atrinuto como Entidad de AccesorioModel que contiene lista. Se llamará a través de "buscarAccesorioPorTipo".
    public ResponseEntity<List<AccesorioModel>> buscarAccesorioPorTipo(@PathVariable("tipo") String tipoAccesorio){
        // Se obtienen accesorios mediante el método "obtenerAccesoriosPorTipo" desde accesorioService.
        List <AccesorioModel> accesorios = accesorioService.obtenerAccesoriosPorTipo(tipoAccesorio);
        if (accesorios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accesorios);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtiene mapeo del precio del Accesorio.
    @GetMapping("/precio/{precio}")
    @Operation(
                summary = "Buscar accesorios por precio.",
                description = "Obtiene una lista de accesorios cuyo precio coincida con el valor especificado."
            )
            @ApiResponses(value = {
                @ApiResponse(
                    responseCode = "200",
                    description = "Accesorios encontrados exitosamente.",
                    content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = AccesorioModel.class))
                    )
                ),
                @ApiResponse(                                                                                                  // GET BY PRECIO.
                    responseCode = "204",
                    description = "No se encontraron accesorios con ese precio."
                )
            })
    // Atrinuto como Entidad de AccesorioModel que contiene lista. Se llamará a través de "buscarAccesorioPorPrecio".
    public ResponseEntity<List<AccesorioModel>> buscarAccesorioPorPrecio(@PathVariable("precio") BigDecimal precioAccesorio){
        // Se obtienen accesorios mediante el método "obtenerAccesoriosPorPrecio" desde accesorioService.
        List<AccesorioModel> accesorios = accesorioService.obtenerAccesorioPorPrecio(precioAccesorio);
        if(accesorios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accesorios);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtiene mapeo del stock del Accesorio.
    @GetMapping("/stock/{stock}")
    @Operation(
                summary = "Buscar accesorios por stock.",
                description = "Obtiene una lista de accesorios que tengan el stock exacto especificado."
            )
            @ApiResponses(value = {
                @ApiResponse(
                    responseCode = "200",
                    description = "Accesorios encontrados exitosamente.",
                    content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = AccesorioModel.class))
                )
            ),
            @ApiResponse(                                                                                                       // GET BY STOCK.
                    responseCode = "204",
                    description = "No se encontraron accesorios con ese stock."
                )
            })
    // Atrinuto como Entidad de AccesorioModel que contiene lista. Se llamará a través de "buscarAccesorioPorStock".
    public ResponseEntity<List<AccesorioModel>> buscarAccesorioPorStock(@PathVariable("stock") Integer stockAccesorio){
        // Se obtienen accesorios mediante el método "obtenerAccesoriosPorStock" desde accesorioService.
        List<AccesorioModel> accesorios = accesorioService.obtenerAccesorioPorStock(stockAccesorio);
        if(accesorios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accesorios);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener mapeo de marca con marca.
    @GetMapping("/marca/{marca}")
    @Operation(
        summary = "Buscar accesorios por marca.",
        description = "Obtiene una lista de accesorios cuya marca coincida con la proporcionada."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Accesorios encontrados exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AccesorioModel.class))
            )
        ),
        @ApiResponse(                                                                                                           // GET BY MARCA.
            responseCode = "204",
            description = "No se encontraron accesorios con esa marca."
        )
    })
    // Atrinuto como Entidad de AccesorioModel que contiene lista. Se llamará a través de "buscarPorMarca".
    public ResponseEntity<List<AccesorioModel>> buscarAccesorioPorMarca(@PathVariable("marca") String marcaAccesorio){
        // Se obtienen accesorios mediante el método "obtenerAccesoriosPorMarca" desde accesorioService.
        List <AccesorioModel> accesorios = accesorioService.obtenerAccesoriosPorMarca(marcaAccesorio);
        if (accesorios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accesorios);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapea un resumen.
    @GetMapping({"/resumen"})
    @Operation(
        summary = "Resumen de accesorios.",
        description = "Obtiene un resumen con datos clave de los accesorios, incluyendo información seleccionada."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de accesorios obtenido exitosamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(
                        type = "object",
                        description = "Mapa con atributos resumidos del accesorio (por ejemplo: ID, nombre, tipo)."
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
    public ResponseEntity<List<Map<String, Object>>> resumenAccesorios(){
        // Devuelve el método obtenerAccesorioConNombres para resumenAccesorio.
        List<Map<String, Object>> resumenAccesorio = accesorioService.obtenerAccesoriosConDatos();
        if(resumenAccesorio.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenAccesorio);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}