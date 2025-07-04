package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipoProductos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoProducto;

    @Column(length = 100, nullable = false, unique = true)
    @NotNull(message = "El tipo de producto requiere de un nombre.")
    private String nombreTipoProducto;

    @OneToMany(mappedBy = "tipoProducto")
    @JsonIgnore
    private List<PeliculaModel> peliculas;

    @OneToMany(mappedBy = "tipoProducto")
    @JsonIgnore
    private List<AccesorioModel> accesorios;

    @OneToMany(mappedBy = "tipoProducto")
    @JsonIgnore
    private List<VideojuegoModel> videojuegos;
}