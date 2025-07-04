package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.PeliculaControllerV2;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PeliculaModelAssembler implements RepresentationModelAssembler<PeliculaModel, EntityModel<PeliculaModel>> {

    @Override
    public EntityModel<PeliculaModel> toModel(PeliculaModel pelicula) {
        /**
         * Este ensamblador convierte un PeliculaModel en un EntityModel que incluye
         * enlaces HATEOAS.
         * Los enlaces permiten a los clientes navegar por la API de manera más
         * intuitiva.
         */
        return EntityModel.of(pelicula,
                linkTo(methodOn(PeliculaControllerV2.class).ObtenerPorIdPeliculas(pelicula.getIdPelicula()))
                        .withSelfRel(), // Enlace al recurso específico de la película
                linkTo(methodOn(PeliculaControllerV2.class).getAllPeliculas()).withRel("peliculas"), // Enlace a la
                                                                                                      // colección de
                                                                                                      // películas
                linkTo(methodOn(PeliculaControllerV2.class).CrearPelicula(null)).withRel("crear"), // Enlace para crear
                                                                                                   // una nueva película
                linkTo(methodOn(PeliculaControllerV2.class).ActualizarPelicula(pelicula.getIdPelicula(), null))
                        .withRel("actualizar"), // Enlace para actualizar la película
                linkTo(methodOn(PeliculaControllerV2.class).ActualizarParcialPelicula(pelicula.getIdPelicula(), null))
                        .withRel("actualizar-parcial"), // Enlace para actualización parcial de la película
                linkTo(methodOn(PeliculaControllerV2.class).deletePelicula(pelicula.getIdPelicula()))
                        .withRel("eliminar película") // Enlace para eliminar la película
        );
    }
}
