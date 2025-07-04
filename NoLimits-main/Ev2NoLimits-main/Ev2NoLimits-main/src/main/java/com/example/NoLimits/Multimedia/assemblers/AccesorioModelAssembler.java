package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.NoLimits.Multimedia.controllerV2.AccesorioControllerV2;
import com.example.NoLimits.Multimedia.model.AccesorioModel;

@Component
public class AccesorioModelAssembler implements RepresentationModelAssembler<AccesorioModel, EntityModel<AccesorioModel>> {

    @Override
    public EntityModel<AccesorioModel> toModel(AccesorioModel accesorio) {
        /**
         * Este ensamblador convierte un AccesorioModel en un EntityModel que incluye enlaces HATEOAS.
         * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
         */
        return EntityModel.of(accesorio,
                linkTo(methodOn(AccesorioControllerV2.class).getAllAccesorios()).withRel("accesorios"), // Enlace a la colección de accesorios
                linkTo(methodOn(AccesorioControllerV2.class).getById(accesorio.getIdAccesorio())).withSelfRel(), // Enlace al recurso específico del accesorio
                linkTo(methodOn(AccesorioControllerV2.class).actualizarAccesorio(accesorio.getIdAccesorio(), null)).withRel("actualizar"), // Enlace para actualizar el accesorio
                linkTo(methodOn(AccesorioControllerV2.class).actualizaAccesorioParcial(accesorio.getIdAccesorio(), null)).withRel("actualizar_parcial"), // Enlace para actualizar parcialmente el accesorio
                linkTo(methodOn(AccesorioControllerV2.class).eliminarAccesorio(accesorio.getIdAccesorio())).withRel("eliminar"), // Enlace para eliminar el accesorio
                linkTo(methodOn(AccesorioControllerV2.class).crearAccesorio(null)).withRel("crear") // Enlace para crear un nuevo accesorio
        );
    }
}
