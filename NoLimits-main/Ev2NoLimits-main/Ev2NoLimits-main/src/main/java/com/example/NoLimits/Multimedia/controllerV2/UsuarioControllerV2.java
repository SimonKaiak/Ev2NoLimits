package com.example.NoLimits.Multimedia.controllerV2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.NoLimits.Multimedia.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import com.example.NoLimits.Multimedia.assemblers.UsuarioModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import com.example.NoLimits.Multimedia.model.UsuarioModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/api/v2/usuarios", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Usuarios-Controller-V2", description = "Operaciones relacionadas con los usuarios.")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
    // Anotación para documentar la operación en Swagger UI.
    @Operation(
        summary = "Listar todos los usuarios (HATEOAS)", // Título visible en la documentación.
        description = "Obtiene una lista de todos los usuarios registrados con enlaces HATEOAS." // Descripción más detallada.
    )

    // Documenta la respuesta HTTP 200 cuando se devuelven usuarios correctamente.
    @ApiResponse(
        responseCode = "200", // Código de éxito.
        description = "Lista de usuarios obtenida exitosamente con enlaces HATEOAS.", // Explicación.
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido con enlaces HATEOAS.
            schema = @Schema(implementation = UsuarioModel.class) // Modelo usado para documentar el contenido.
        )
    )

    // Documenta el caso en que no hay usuarios en la base de datos.
    @ApiResponse(
        responseCode = "204", // Código HTTP para "No Content".
        description = "No hay usuarios registrados.", // Mensaje que aparecerá en Swagger.
        content = @Content // Sin cuerpo en la respuesta.
    )

    // Define que este método responde a GET y devuelve un HAL+JSON (con enlaces HATEOAS).
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<UsuarioModel>> getAllUsuarios() {                                                            // GET ALL.
    // Obtiene la lista de todos los usuarios desde la base de datos.
    List<UsuarioModel> usuarios = usuarioService.findAll();

    // Si la lista está vacía, lanza una excepción que devuelve HTTP 204 (sin contenido).
    if (usuarios.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay usuarios registrados.");
    }

    // Por cada usuario, oculta la contraseña y convierte a EntityModel con enlaces HATEOAS.
    List<EntityModel<UsuarioModel>> usuariosConLinks = usuarios.stream()
            .peek(u -> u.setPassword("********")) // Protege la contraseña en la respuesta.
            .map(assembler::toModel) // Aplica el assembler que agrega los enlaces HATEOAS.
            .collect(Collectors.toList());

    // Retorna una CollectionModel (conjunto de EntityModels) y agrega un enlace "self" a la colección.
    return CollectionModel.of(
            usuariosConLinks, // Lista de usuarios con HATEOAS.
            linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withSelfRel() // Enlace a sí mismo.
    );
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación Swagger: resumen breve del endpoint.
    @Operation(
        summary = "Buscar usuario por ID (HATEOAS).", // Título visible en Swagger UI.
        description = "Obtiene un usuario específico por su ID con enlaces HATEOAS." // Explicación más detallada.
    )

    // Respuesta esperada cuando el usuario se encuentra exitosamente.
    @ApiResponse(
        responseCode = "200", // Código HTTP 200 (OK).
        description = "Usuario encontrado exitosamente con enlaces HATEOAS.", // Descripción de la respuesta.
        content = @Content(
            mediaType = "application/hal+json", // Formato de la respuesta con HATEOAS.
            schema = @Schema(implementation = UsuarioModel.class) // Modelo devuelto (Usuario).
        )
    )

    // Respuesta cuando no se encuentra el usuario.
    @ApiResponse(
        responseCode = "404", // Código HTTP 404 (Not Found).
        description = "Usuario no encontrado." // Descripción de la situación.                                             
    )

    // Indica que este endpoint responde a GET con un ID como parámetro de ruta.
    // Y devuelve un JSON con enlaces HATEOAS.
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<UsuarioModel> getUsuarioById(@PathVariable Long id) {                                                        // GET BY ID.                           
        // Busca el usuario por ID usando el servicio.
        UsuarioModel usuario = usuarioService.findById(id);

    // Si no lo encuentra, lanza una excepción con estado 404 (y mensaje).
    if (usuario == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    }

    // Por seguridad, oculta la contraseña real del usuario.
    usuario.setPassword("********");

    // Convierte el objeto UsuarioModel en un EntityModel con enlaces HATEOAS y lo retorna.
    return assembler.toModel(usuario);
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Anotación de Swagger para describir el propósito general del endpoint.
    @Operation(
        summary = "Guardar Usuario (HATEOAS)", // Título breve para Swagger UI.
        description = "Guarda un nuevo usuario en la base de datos y devuelve el recurso con enlaces HATEOAS." // Descripción detallada.
    )
    // Respuesta esperada cuando el usuario se crea exitosamente.
    @ApiResponse(
        responseCode = "201", // Código HTTP 201 (Created).
        description = "Usuario creado exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Tipo de contenido que se devuelve (HAL con JSON).
            schema = @Schema(implementation = UsuarioModel.class) // Modelo de datos que se devuelve.
        )
    )
    // Respuesta esperada cuando hay un error de validación o problema al crear.
    @ApiResponse(
        responseCode = "400", // Código HTTP 400 (Bad Request).
        description = "Error al crear el usuario.",
        content = @Content // Contenido vacío para indicar que no se retorna un cuerpo específico.
    )
    // Indica que este método manejará solicitudes POST y devolverá datos HATEOAS.                                                  
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UsuarioModel>> createUsuario(@RequestBody UsuarioModel usuario) {                            // POST.
        // Guarda el usuario en la base de datos usando el servicio.
        UsuarioModel createdUsuario = usuarioService.save(usuario);

    // Crea un EntityModel con enlaces HATEOAS usando el assembler.
    EntityModel<UsuarioModel> entityModel = assembler.toModel(createdUsuario);

    // Retorna el recurso creado con:
    // - Código 201 (Created)
    // - Header Location apuntando al nuevo recurso
    // - Cuerpo con el usuario y sus enlaces HATEOAS
    return ResponseEntity
            .created(
                linkTo(methodOn(UsuarioControllerV2.class) // Construye el enlace al endpoint de obtener por ID
                        .getUsuarioById(createdUsuario.getId())).toUri() // Convierte ese enlace a URI
            )
            .body(entityModel); // Retorna el cuerpo del nuevo usuario con enlaces
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Anotación de Swagger que describe el propósito del endpoint.
    @Operation(
        summary = "Actualizar Usuario (HATEOAS)", // Título breve visible en Swagger UI.
        description = "Actualiza un usuario existente por su ID y devuelve el recurso actualizado con enlaces HATEOAS." // Descripción más larga.
    )

    // Documentación de la respuesta exitosa.
    @ApiResponse(
        responseCode = "200", // Código HTTP 200 OK.
        description = "Usuario actualizado exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Indica que se retorna HATEOAS (HAL + JSON).
            schema = @Schema(implementation = UsuarioModel.class) // Swagger usará UsuarioModel para documentar el contenido.
        )
    )

    // Documentación del caso en que el usuario no es encontrado.
    @ApiResponse(
        responseCode = "404", // Código HTTP 404 Not Found.
        description = "Usuario no encontrado.",
        content = @Content // No se devuelve cuerpo específico en este caso.
    )

    // Define que este método responde a PUT y devuelve un EntityModel con media type HATEOAS.
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UsuarioModel>> updateUsuario(                                                                     // PUT.
        @PathVariable Long id, // ID del usuario a actualizar.
        @RequestBody UsuarioModel usuario // Cuerpo con los nuevos datos.
    ) {
    // Asegura que el ID del usuario que viene en el body coincida con el de la ruta.
    usuario.setId(id);

    // Busca si el usuario existe.
    UsuarioModel usuarioExistente = usuarioService.findById(id);

    // Actualiza solo los campos que vienen con datos (evita sobrescribir con null).
    if (usuario.getApellidos() != null) {
        usuarioExistente.setApellidos(usuario.getApellidos());
    }
    if (usuario.getCorreo() != null) {
        usuarioExistente.setCorreo(usuario.getCorreo());
    }
    if (usuario.getNombre() != null) {
        usuarioExistente.setNombre(usuario.getNombre());
    }
    if (usuario.getTelefono() != null) {
        usuarioExistente.setTelefono(usuario.getTelefono());
    }
    if (usuario.getPassword() != null) {
        usuarioExistente.setPassword(usuario.getPassword());
    }

    // Guarda los cambios en la base de datos.
    UsuarioModel updatedUsuario = usuarioService.save(usuarioExistente);

    // Devuelve el usuario actualizado con enlaces HATEOAS.
    return ResponseEntity.ok(assembler.toModel(updatedUsuario));
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Anotación para documentar en Swagger el propósito del endpoint PATCH.
    @Operation(
        summary = "Actualizar Usuario Parcialmente (HATEOAS)", // Título breve para Swagger UI.
        description = "Actualiza parcialmente un usuario existente por su ID y devuelve el recurso actualizado con enlaces HATEOAS." // Explicación más detallada.
    )

    // Respuesta esperada cuando la operación es exitosa.
    @ApiResponse(
        responseCode = "200", // Código HTTP 200 OK.
        description = "Usuario actualizado parcialmente exitosamente con enlaces HATEOAS.",
        content = @Content(
            mediaType = "application/hal+json", // Formato con enlaces HATEOAS.
            schema = @Schema(implementation = UsuarioModel.class) // Modelo de usuario para documentar la estructura.
        )
    )

    // Respuesta si el usuario no fue encontrado.
    @ApiResponse(
        responseCode = "404", // Código HTTP 404 Not Found.
        description = "Usuario no encontrado.",
        content = @Content // No se especifica un cuerpo en este caso.
    )

    // Define que este método maneja solicitudes PATCH y devuelve HATEOAS.
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UsuarioModel>> patchUsuario(@PathVariable("id") Long id,                                      // PATCH.
                                                              @RequestBody UsuarioModel usuarioParcial) {
    // Busca si el usuario existe.
    UsuarioModel usuarioExistente = usuarioService.findById(id);

    // Si no existe, lanza un error 404.
    if (usuarioExistente == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    }

    // Actualiza solo los campos que vienen con valores (evita sobrescribir con null).
    if (usuarioParcial.getApellidos() != null) {
        usuarioExistente.setApellidos(usuarioParcial.getApellidos());
    }
    if (usuarioParcial.getCorreo() != null) {
        usuarioExistente.setCorreo(usuarioParcial.getCorreo());
    }
    if (usuarioParcial.getNombre() != null) {
        usuarioExistente.setNombre(usuarioParcial.getNombre());
    }
    if (usuarioParcial.getTelefono() != null) {
        usuarioExistente.setTelefono(usuarioParcial.getTelefono());
    }
    if (usuarioParcial.getPassword() != null) {
        usuarioExistente.setPassword(usuarioParcial.getPassword());
    }

    // Guarda los cambios.
    UsuarioModel usuarioActualizado = usuarioService.save(usuarioExistente);

    // Devuelve el usuario con enlaces HATEOAS.
    return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Documentación para Swagger: describe la funcionalidad del endpoint DELETE.
    @Operation(
        summary = "Eliminar Usuario (HATEOAS)", // Título que aparece en Swagger UI.
        description = "Elimina un usuario existente por su ID." // Explicación más detallada.
    )

    // Respuesta esperada cuando se elimina correctamente.
    @ApiResponse(
        responseCode = "204", // Código HTTP 204 (No Content).
        description = "Usuario eliminado exitosamente." // Mensaje que se muestra en Swagger.
    )

    // Respuesta cuando el usuario no fue encontrado.
    @ApiResponse(
        responseCode = "404", // Código HTTP 404 (Not Found).
        description = "Usuario no encontrado." // Descripción del error.
    )

    // Define que este método maneja DELETE y produce contenido HATEOAS (aunque aquí no se devuelve cuerpo).
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {                                                               // DELETE.
    try {
            usuarioService.deletebyId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
