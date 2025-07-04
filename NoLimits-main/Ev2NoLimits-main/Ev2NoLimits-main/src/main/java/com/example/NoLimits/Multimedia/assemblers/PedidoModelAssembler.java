package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.PedidoControllerV2;
import com.example.NoLimits.Multimedia.model.PedidoModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<PedidoModel, EntityModel<PedidoModel>> {

    @Override
    public EntityModel<PedidoModel> toModel(PedidoModel pedido) {
        /**
         * Este ensamblador convierte un PedidoModel en un EntityModel que incluye enlaces HATEOAS.
         * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
         */
        return EntityModel.of(pedido,
                linkTo(methodOn(PedidoControllerV2.class).BuscarPedidoPorId(pedido.getId())).withSelfRel(), // Enlace al pedido específico
                linkTo(methodOn(PedidoControllerV2.class).ObtenerTodosLosPedidos()).withRel("pedidos"), // Enlace a todos los pedidos
                linkTo(methodOn(PedidoControllerV2.class).CrearPedido(null)).withRel("crear"), // Enlace para crear un nuevo pedido
                linkTo(methodOn(PedidoControllerV2.class).ActualizarPedido(pedido.getId(), null)).withRel("actualizar"), // Enlace para actualizar el pedido
                linkTo(methodOn(PedidoControllerV2.class).actualizar_parcial_pelicula(pedido.getId(), null)).withRel("actualizar-parcial"), // Enlace para actualización parcial del pedido
                linkTo(methodOn(PedidoControllerV2.class).EliminarPedido(pedido.getId())).withRel("eliminar") // Enlace para eliminar el pedido
        );
    }
}