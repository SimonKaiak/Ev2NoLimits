package com.example.NoLimits.Multimedia.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.DetalleVentaControllerV2;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;

@Component
public class DetalleVentaModelAssembler implements RepresentationModelAssembler<DetalleVentaModel, EntityModel<DetalleVentaModel>> {
    @Override
    public EntityModel<DetalleVentaModel> toModel(DetalleVentaModel detalleVenta) {
        /**
         * Este ensamblador convierte un DetalleVentaModel en un EntityModel que incluye enlaces HATEOAS.
         * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
         */
        return EntityModel.of(detalleVenta,
                linkTo(methodOn(DetalleVentaControllerV2.class).GetAllDetallesVenta()).withRel("detalles_pedido"), // Enlace a todos los detalles de venta
                linkTo(methodOn(DetalleVentaControllerV2.class).GetById(detalleVenta.getIdDetalleVenta())).withSelfRel(), // Enlace al detalle de venta específico
                linkTo(methodOn(DetalleVentaControllerV2.class).ActualizarDetalleVenta(detalleVenta.getIdDetalleVenta(), null)).withRel("actualizar"), // Enlace para actualizar el detalle de venta
                linkTo(methodOn(DetalleVentaControllerV2.class).actualizaDetalleVentaParcial(detalleVenta.getIdDetalleVenta(), null)).withRel("actualizar_parcial"), // Enlace para actualización parcial del detalle de venta
                linkTo(methodOn(DetalleVentaControllerV2.class).EliminarDetalleVenta(detalleVenta.getIdDetalleVenta())).withRel("eliminar"), // Enlace para eliminar el detalle de venta
                linkTo(methodOn(DetalleVentaControllerV2.class).CrearDetalleVenta(null)).withRel("crear") // Enlace para crear un nuevo detalle de venta
        );
    }
}
