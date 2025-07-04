package com.example.NoLimits.Multimedia.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.DetallePedidoControllerV2;
import com.example.NoLimits.Multimedia.model.DetallePedidoModel;

@Component
public class DetallePedidoModelAssembler implements RepresentationModelAssembler<DetallePedidoModel, EntityModel<DetallePedidoModel>> {
    /**
     * Este ensamblador convierte un DetallePedidoModel en un EntityModel que incluye enlaces HATEOAS.
     * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
     */
    @Override
    public EntityModel<DetallePedidoModel> toModel(DetallePedidoModel detallePedido) { // Método para convertir un DetallePedidoModel a EntityModel con enlaces HATEOAS
        return EntityModel.of(detallePedido, // Crear un EntityModel a partir del DetallePedidoModel
                linkTo(methodOn(DetallePedidoControllerV2.class).GetAllDetallesPedidos()).withRel("detalles_pedido"), // Enlace a todos los detalles de pedido
                linkTo(methodOn(DetallePedidoControllerV2.class).GetById(detallePedido.getIdDetallePedido())).withSelfRel(), // Enlace al detalle de pedido específico
                linkTo(methodOn(DetallePedidoControllerV2.class).ActualizarDetallePedido(detallePedido.getIdDetallePedido(), null)).withRel("actualizar"), // Enlace para actualizar el detalle de pedido
                linkTo(methodOn(DetallePedidoControllerV2.class).actualizaDetallePedidoParcial(detallePedido.getIdDetallePedido(), null)).withRel("actualizar_parcial"), // Enlace para actualización parcial del detalle de pedido
                linkTo(methodOn(DetallePedidoControllerV2.class).EliminarDetallePedido(detallePedido.getIdDetallePedido())).withRel("eliminar"), // Enlace para eliminar el detalle de pedido
                linkTo(methodOn(DetallePedidoControllerV2.class).CrearDetallePedido(null)).withRel("crear") // Enlace para crear un nuevo detalle de pedido
        );
    }
}
