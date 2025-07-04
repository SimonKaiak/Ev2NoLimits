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
@Table(name = "peliculas")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PeliculaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPelicula;

    @Column(length = 100, nullable = false)
    @NotNull(message = "Es necesario que la película posea un nombre.")
    private String nombrePelicula;

    @Column(length = 50, nullable = false)
    @NotNull(message = "Es necesario que la película esté asociada a una categoría.")
    private String categoriaPelicula;

    @Column(length = 50, nullable = false)
    @NotNull(message = "Es necesario que la película esté asociada a una plataforma.")
    private String plataformaPelicula;

    @Column(nullable = false)
    @NotNull(message = "Es necesario que la película tenga una duración.")
    private Integer duracionPelicula;

    @Column(length = 500, nullable = false)
    @NotNull(message = "Es necesario que la película tenga una descripción.")
    private String descripcionPelicula;

    @Column(nullable = false)
    @NotNull(message = "Es necesario que la película tenga un precio.")
    private Float precioPelicula;

    // Significa que la relación es obligatoria.
    @ManyToOne(optional = true)
    @JoinColumn(name = "id_tipo_producto")
    // Para evitar bucle.
    @JsonIgnore
    // @NotNull(message = "El tipo de producto es obligatorio")
    private TipoProductoModel tipoProducto;

    @OneToMany(mappedBy = "pelicula")
    private List<DetallePedidoModel> detallePedidos;

    @OneToMany(mappedBy = "pelicula")
    private List<DetalleVentaModel> detalleVentas;

    @OneToMany(mappedBy = "pelicula")
    private List<AccesorioModel> accesorios;

    @Column(nullable = false)
    private boolean activa = true;

}
