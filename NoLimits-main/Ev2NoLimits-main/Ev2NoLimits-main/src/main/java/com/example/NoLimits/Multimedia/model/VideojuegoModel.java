package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videojuegos")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class VideojuegoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVideojuego;

    @Column(length = 50, nullable = false)
    @NotNull(message = "El videojuego requiere de un nombre.")
    private String nombreVideojuego;

    @Column(length = 50, nullable = false)
    @NotNull(message = "El videojuego requiere de una categoría.")
    private String categoriaVideojuego;

    @Column(length = 50, nullable = false)
    @NotNull(message = "El videojuego requiere de una plataforma.")
    private String plataformaVideojuego;

    @Column(length = 500, nullable = false)
    @NotNull(message = "El videojuego requiere de un desarrollador.")
    private String desarrolladorVideojuego;

    @Column(length = 500, nullable = false)
    @NotNull(message = "El videojuego requiere de una descripción.")
    private String descripcionVideojuego;

    @Column(nullable = false)
    @NotNull(message = "El videojuego requiere de un precio.")
    private Float precioVideojuego;

    // Significa que la relación es obligatoria.
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo_producto")
    // Para evitar bucle.
    @JsonIgnore
    @NotNull(message = "El tipo de producto es obligatorio")
    private TipoProductoModel tipoProducto;

    @OneToMany(mappedBy = "videojuego")
    private List<DetallePedidoModel> detallePedidos;

    @OneToMany(mappedBy = "videojuego")
    private List<DetalleVentaModel> detalleVentas;

    @OneToMany(mappedBy = "videojuego")
    private List<AccesorioModel> accesorios;
}
