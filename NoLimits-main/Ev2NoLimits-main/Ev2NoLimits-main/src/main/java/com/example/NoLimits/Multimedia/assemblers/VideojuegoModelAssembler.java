package com.example.NoLimits.Multimedia.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.VideojuegoControllerV2;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;

import org.springframework.hateoas.server.RepresentationModelAssembler;

@Component
public class VideojuegoModelAssembler implements RepresentationModelAssembler<VideojuegoModel, EntityModel<VideojuegoModel>> {

        @SuppressWarnings("null") 
        @Override
        public EntityModel toModel(VideojuegoModel videoJuego) {
                /**
                 * Este ensamblador convierte un VideojuegoModel en un EntityModel que incluye enlaces HATEOAS.
                 * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
                 */
                return EntityModel.of(videoJuego,
                                linkTo(methodOn(VideojuegoControllerV2.class).getVideojuegoById(videoJuego.getIdVideojuego())).withSelfRel(), // Enlace al recurso específico del videojuego
                                linkTo(methodOn(VideojuegoControllerV2.class).getAllVideojuegos()).withRel("videojuegos"), // Enlace a todos los videojuegos
                                linkTo(methodOn(VideojuegoControllerV2.class).crearVideojuego(videoJuego)).withRel("crear"), // Enlace para crear un nuevo videojuego
                                linkTo(methodOn(VideojuegoControllerV2.class).updateVideoJuego(videoJuego.getIdVideojuego(), null)).withRel("actualizar"), // Enlace para actualizar el videojuego
                                linkTo(methodOn(VideojuegoControllerV2.class).patchVideojuego(videoJuego.getIdVideojuego(), null)).withRel("actualizar-parcial"), // Enlace para actualización parcial del videojuego
                                linkTo(methodOn(VideojuegoControllerV2.class).deleteVideoJuego(videoJuego.getIdVideojuego())).withRel("eliminar"));
        }
}
// Este método es necesario para que el assembler funcione correctamente con los
// parámetros adicionales.
