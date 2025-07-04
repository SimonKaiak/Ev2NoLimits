package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;
import com.example.NoLimits.Multimedia.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios-Controller.", description = "Operaciones relacionadas con los usuarios.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Aquí puedes agregar los métodos para manejar las solicitudes HTTP
    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            // @Content: Especifica el tipo de contenido de la solicitud o respuesta de la
            // API.
            // mediaType = "application/json": Indica que el contenido está en formato JSON.
            content = @Content(mediaType = "application/json",
                    // Indica que la estructura del JSON se basa en la clase AccesorioModel.
                    // @Schema: Proporciona información adicional sobre el modelo de datos.
                    schema = @Schema(implementation = UsuarioModel.class)
            // ---------------------------------------------------------------------------------------------
            // Swagger usará esta clase para generar automáticamente la documentación del
            // modelo de datos. GET ALL.
            // ---------------------------------------------------------------------------------------------
            ))
    public ResponseEntity<List<UsuarioModel>> listarUsuarios() {
        List<UsuarioModel> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        usuarios.forEach(u -> u.setPassword("********")); // Ocultar la contraseña

        return ResponseEntity.ok(usuarios);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Buscar por Nombre.
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar usuarios por nombre.", description = "Obtiene una lista de usuarios que coincidan con el nombre proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioModel.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron usuarios con ese nombre." // GET BY
                                                                                                          // NOMBRE.
            )
    })
    // Atrinuto como Entidad de UsuarioModel que contiene lista. Se llamará a través
    // de "buscarUsuarioPorNombre".
    // PathVariable: Captura valores de una URL.
    public ResponseEntity<List<UsuarioModel>> buscarUsuarioPorNombre(@PathVariable("nombre") String nombreUsuario) {
        // Se obtienen usuarios mediante el método "obtenerUsuariosPorNombre" desde
        // usuarioService.
        List<UsuarioModel> usuarios = usuarioService.obtenerUsuarioPorNombre(nombreUsuario);
        // Si usuarios está vacío:.
        if (usuarios.isEmpty()) {
            // Devuelve error.
            return ResponseEntity.noContent().build();
        }
        // Sino devuelve okey.
        return ResponseEntity.ok(usuarios);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Buscar por Correo.
    @GetMapping("/correo/{correo}")
    @Operation(summary = "Buscar usuarios por correo.", description = "Obtiene una lista de usuarios que coincidan con el correo electrónico proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioModel.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron usuarios con ese correo." // GET BY
                                                                                                          // CORREO.
            )
    })
    // Atrinuto como Entidad de UsuarioModel que contiene lista. Se llamará a través
    // de "buscarUsuarioPorCorreo".
    // PathVariable: Captura valores de una URL.
    public ResponseEntity<List<UsuarioModel>> buscarUsuarioPorCorreo(@PathVariable("correo") String correoUsuario) {
        // Se obtienen usuarios mediante el método "obtenerUsuariosPorCorreo" desde
        // usuarioService.
        List<UsuarioModel> usuarios = usuarioService.obtenerUsuarioPorCorreo(correoUsuario);
        // Si usuarios está vacío:.
        if (usuarios.isEmpty()) {
            // Devuelve error.
            return ResponseEntity.noContent().build();
        }
        // Sino devuelve okey.
        return ResponseEntity.ok(usuarios);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene un usuario específico por su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    public ResponseEntity<UsuarioModel> buscarUsuarioPorId(@PathVariable long id) {
        try {
            UsuarioModel usuario = usuarioService.findById(id);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            } // GET BY ID.
            usuario.setPassword("********"); // Ocultar la contraseña
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Manejo de errores
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Crea un objeto.
    @PostMapping
    @Operation(summary = "Guardar usuario.", description = "Guarda un nuevo usuario en la base de datos.")
    @ApiResponse(responseCode = "201", description = "Usuario guardado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    @ApiResponse(responseCode = "400", description = "Error al guardar el usuario.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    // @Operation: Anotación de Swagger para documentar la operación del
    // controlador.
    public ResponseEntity<UsuarioModel> guardarUsuario(@RequestBody UsuarioModel usuario) { // POST.
        // Devuelve el método guardar usuario para nuevoUsuario.
        UsuarioModel nuevoUsuario = usuarioService.save(usuario);
        // Se creó exitosamente un usuario.
        return ResponseEntity.status(201).body(nuevoUsuario);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario.", description = "Actualiza un usuario existente por su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    public ResponseEntity<UsuarioModel> editarUsuario(@PathVariable long id, @RequestBody UsuarioModel usuario) {
        try {
            UsuarioModel usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            if (usuarioActualizado == null) {
                return ResponseEntity.notFound().build(); // PUT.
            }
            usuarioActualizado.setPassword("**"); // Ocultar la contraseña
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Manejo de errores
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Método para actualizar por id específicamente algo.
    @PatchMapping({ "/{id}" })
    @Operation(summary = "Actualizar usuario parcialmente.", description = "Actualiza parcialmente un usuario existente por su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado parcialmente exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))

    // PATCH.

    public ResponseEntity<UsuarioModel> editarUsuarioParcial(@PathVariable("id") Long id,
            @RequestBody UsuarioModel usuarioModel) {
        UsuarioModel usuarioActualizado = usuarioService.patchUsuarioModel(id, usuarioModel);
        return usuarioActualizado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(usuarioActualizado);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario.", description = "Elimina un usuario existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente.")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.") 

    public ResponseEntity<Void> eliminarUsuarioPorId(@PathVariable Long id) {
        usuarioService.deletebyId(id);
        return ResponseEntity.noContent().build(); 
    }
// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Mapea un resumen.
    @GetMapping({ "/resumen" })
    @Operation(summary = "Resumen de usuarios.", description = "Obtiene un resumen con información clave de los usuarios, como ID, nombre, correo, rol, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen de usuarios obtenido exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "object", description = "Mapa con atributos resumidos del usuario (por ejemplo: ID, nombre, correo, rol).")))), // GET
                                                                                                                                                                                                                                                                                                                           // BY
                                                                                                                                                                                                                                                                                                                           // RESUMEN-
            @ApiResponse(responseCode = "204", description = "No hay datos de resumen disponibles.")
    })
    // Map: Estructura de datos.
    // Object: Objeto creado que almacena cualquier dato (para Map).
    public ResponseEntity<List<Map<String, Object>>> resumenUsuarios() {
        // Devuelve el método resumenUsuarios para resumen.
        List<Map<String, Object>> resumenUsuarios = usuarioService.obtenerUsuariosConDatos();
        if (resumenUsuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumenUsuarios);
    }
    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
