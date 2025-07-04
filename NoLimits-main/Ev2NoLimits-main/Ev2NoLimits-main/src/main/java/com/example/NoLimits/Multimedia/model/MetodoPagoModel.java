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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "metodos_Pago")
@Data
@NoArgsConstructor  
@AllArgsConstructor
public class MetodoPagoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String nombre;

    // Rompe bucle de error 500 que provoca el json de usuario model
    // evita que la lista de ventas se incluya cuando generas el json del usuario.

    @JsonIgnore
    // MappedBy: se situa en donde no tiene la FK
    @OneToMany(mappedBy = "metodoPagoModel")
    private List<VentasModel> ventas;
}
