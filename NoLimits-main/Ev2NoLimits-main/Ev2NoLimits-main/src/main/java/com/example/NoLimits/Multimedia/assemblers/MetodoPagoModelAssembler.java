package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.MetodoPagoControllerV2;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MetodoPagoModelAssembler implements RepresentationModelAssembler<MetodoPagoModel, EntityModel<MetodoPagoModel>> {

    @Override
    public EntityModel<MetodoPagoModel> toModel(MetodoPagoModel metodoPago) {
        /**
         * Este ensamblador convierte un MetodoPagoModel en un EntityModel que incluye enlaces HATEOAS.
         * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
         */
        return EntityModel.of(metodoPago,
                linkTo(methodOn(MetodoPagoControllerV2.class).ObtenerMetodoDePagoPorId(metodoPago.getId())).withSelfRel(), // Enlace al método de pago específico
                linkTo(methodOn(MetodoPagoControllerV2.class).ObtenerTodosMetodosPago()).withRel("metodos_pago"), // Enlace a todos los métodos de pago
                linkTo(methodOn(MetodoPagoControllerV2.class).Actualizar_MetodoPago(metodoPago.getId(), null)).withRel("actualizar"), // Enlace para actualizar el método de pago
                linkTo(methodOn(MetodoPagoControllerV2.class).actualiza_parcial_MetodoPago(metodoPago.getId(), null)).withRel("actualizar_parcial"), // Enlace para actualización parcial del método de pago
                linkTo(methodOn(MetodoPagoControllerV2.class).EliminarMetodoPago(metodoPago.getId())).withRel("eliminar"), // Enlace para eliminar el método de pago
                linkTo(methodOn(MetodoPagoControllerV2.class).CrearMetodoPago(null)).withRel("crear") // Enlace para crear un nuevo método de pago
        );
    }
}