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
@Table(name = "detalle_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetallePedido;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Float precioUnitario;

    @Column(nullable = false)
    private Float subtotal;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "Es necesario que exista un pedido.")
    private PedidoModel pedido;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "accesorio_id", nullable = true)
    private AccesorioModel accesorio;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "pelicula_id", nullable = true)
    private PeliculaModel pelicula;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "videojuego_id", nullable = true)
    private VideojuegoModel videojuego;
}
