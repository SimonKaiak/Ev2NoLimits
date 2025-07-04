package com.example.NoLimits.Multimedia.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.TipoProductoControllerV2;
import com.example.NoLimits.Multimedia.model.TipoProductoModel;

@Component
public class TipoProductoModelAssembler implements RepresentationModelAssembler<TipoProductoModel, EntityModel<TipoProductoModel>> {
    @Override
    public EntityModel<TipoProductoModel> toModel(TipoProductoModel tipoProducto) {
        /**
         * Este ensamblador convierte un TipoProductoModel en un EntityModel que incluye enlaces HATEOAS.
         * Los enlaces permiten a los clientes navegar por la API de manera más intuitiva.
         */
        return EntityModel.of(tipoProducto,
                linkTo(methodOn(TipoProductoControllerV2.class).GetAllTipoProductos()).withRel("tipos_producto"), // Enlace a todos los tipos de productos
                linkTo(methodOn(TipoProductoControllerV2.class).GetById(tipoProducto.getIdTipoProducto())).withSelfRel(), // Enlace al tipo de producto específico
                linkTo(methodOn(TipoProductoControllerV2.class).actualizarTipoProducto(tipoProducto.getIdTipoProducto(), null)).withRel("actualizar"), // Enlace para actualizar el tipo de producto
                linkTo(methodOn(TipoProductoControllerV2.class).actualizaTipoProductoParcial(tipoProducto.getIdTipoProducto(), null)).withRel("actualizar_parcial"), // Enlace para actualización parcial del tipo de producto
                linkTo(methodOn(TipoProductoControllerV2.class).EliminarTipoProducto(tipoProducto.getIdTipoProducto())).withRel("eliminar"), // Enlace para eliminar el tipo de producto
                linkTo(methodOn(TipoProductoControllerV2.class).CrearTipoProducto(null)).withRel("crear") // Enlace para crear un nuevo tipo de producto
        );
    }
}
