package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Constructor para los NotNull
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

// Clase como entidad.
@Entity
// Nombre entidad.
@Table(name = "accesorios")
// Creación de Getters and Setters.
@Data
// Genera constructores.
@AllArgsConstructor
// Constructores vacíos.
@NoArgsConstructor

public class AccesorioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAccesorio;

    @Column(length = 50, nullable = false)
    private String nombreAccesorio;

    @Column(nullable = false)
    private String tipoAccesorio;

    @Column(length = 50, nullable = false)
    private String marcaAccesorio;

    @Column(length = 500, nullable = false)
    private String descripcionAccesorio;

    @Column(nullable = false)
    private Float precioAccesorio;

    @Column(nullable = false)
    private Integer stockAccesorio;

    // Manda mensaje si quieres crear un accesorio y no tienes creado película.
    @ManyToOne
    @JoinColumn(name = "pelicula_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "Es necesario que exista una película.")
    private PeliculaModel pelicula;

    // Relación de muchos a uno. Muchos accesorios están asociados a un videojeugo.
    // Manda mensaje si quieres crear un accesorio y no tienes creado videojuego.
    @ManyToOne
    @JoinColumn(name = "videojuego_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "Es necesario que exista un videojuego.")
    private VideojuegoModel videojuego;

    // Significa que la relación es obligatoria.
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo_producto", nullable = false)
    @NotNull(message = "El tipo de producto es obligatorio.")
    @JsonIgnore
    private TipoProductoModel tipoProducto;

    @OneToMany(mappedBy = "accesorio")
    private List<DetallePedidoModel> detallePedidos;

    @OneToMany(mappedBy = "accesorio")
    private List<DetalleVentaModel> detalleVentas;
}
