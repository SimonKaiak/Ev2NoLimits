package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.example.NoLimits.Multimedia.controllerV2.VentasControllerV2;
import com.example.NoLimits.Multimedia.model.VentasModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VentasModelAssembler implements RepresentationModelAssembler<VentasModel, EntityModel<VentasModel>> {

    @Override
    public EntityModel<VentasModel> toModel(VentasModel ventas) {
        /**
         * Este ensamblador convierte un VentasModel en un EntityModel que incluye enlaces HATEOAS.
         * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
         */
        return EntityModel.of(ventas,
                linkTo(methodOn(VentasControllerV2.class).getVentaById(ventas.getId())).withSelfRel(), // Enlace al recurso específico de la venta
                linkTo(methodOn(VentasControllerV2.class).getAllVentas()).withRel("ventas"), // Enlace a la colección de ventas
                linkTo(methodOn(VentasControllerV2.class).getVentaByUsuario(ventas.getUsuarioModel().getId())).withRel("ventasPorUsuario"), // Enlace a las ventas por usuario
                linkTo(methodOn(VentasControllerV2.class).getVentaByMetodoPago(ventas.getMetodoPagoModel().getId())).withRel("ventasPorMetodoPago"), // Enlace a las ventas por método de pago
                linkTo(methodOn(VentasControllerV2.class).createVenta(ventas)).withRel("crear"), // Enlace para crear una nueva venta
                linkTo(methodOn(VentasControllerV2.class).updateVenta(ventas.getId(), ventas)).withRel("actualizar"), // Enlace para actualizar la venta
                linkTo(methodOn(VentasControllerV2.class).deleteVenta(ventas.getId())).withRel("eliminar")); // Enlace para eliminar la venta
    }
}