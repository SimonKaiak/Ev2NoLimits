package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.UsuarioControllerV2;
import com.example.NoLimits.Multimedia.model.UsuarioModel;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioModel, EntityModel<UsuarioModel>> {
    @Override
    public EntityModel<UsuarioModel> toModel(UsuarioModel usuario) {
        /**
         * Este ensamblador convierte un UsuarioModel en un EntityModel que incluye enlaces HATEOAS.
         * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
         */
        usuario.setPassword("********");
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioControllerV2.class).getUsuarioById(usuario.getId())).withSelfRel(), // Enlace al recurso específico del usuario
                linkTo(methodOn(UsuarioControllerV2.class).updateUsuario(usuario.getId(), null)).withRel("actualizar"), // Enlace para actualizar el usuario
                linkTo(methodOn(UsuarioControllerV2.class).patchUsuario(usuario.getId(), null)).withRel("actualizar_parcial"), // Enlace para actualización parcial del usuario
                linkTo(methodOn(UsuarioControllerV2.class).deleteUsuario(usuario.getId())).withRel("eliminar"), // Enlace para eliminar el usuario
                linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("usuarios"), // Enlace a todos los usuarios
                linkTo(methodOn(UsuarioControllerV2.class).createUsuario(null)).withRel("crear")); // Enlace para crear un nuevo usuario

    }
}
