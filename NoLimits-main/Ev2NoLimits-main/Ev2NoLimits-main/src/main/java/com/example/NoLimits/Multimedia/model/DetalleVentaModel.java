package com.example.NoLimits.Multimedia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDetalleVenta;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Float precioUnitario;

    @Column(nullable = false)
    private Float subtotal;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    // Para evitar bucle.
    @JsonIgnore
    @NotNull(message = "Es necesario que exista una venta.")
    private VentasModel venta;

    @ManyToOne
    @JoinColumn(name = "accesorio_id", nullable = true)
    // Para evitar bucle.
    @JsonIgnore
    private AccesorioModel accesorio;

    @ManyToOne
    @JoinColumn(name = "pelicula_id", nullable = true)
    // Para evitar bucle.
    @JsonIgnore
    private PeliculaModel pelicula;

    @ManyToOne
    @JoinColumn(name = "videojuego_id", nullable = true)
    // Para evitar bucle.
    @JsonIgnore
    private VideojuegoModel videojuego;
}
