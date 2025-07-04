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
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "El usuario requiere de un nombre.")
    private String nombre;

    @Column(nullable = false)
    @NotNull(message = "El usuario requiere de sus apellidos.")
    private String apellidos;

    @Column(nullable=false)
    @NotNull(message = "El usuario requiere de un correo.")
    private String correo;

    @Column(nullable = false)
    @NotNull(message = "El usuario requiere de un telefono.")
    private Integer telefono;

    @Column(unique = true, length=10, nullable=false)
    @NotNull(message = "El usuario requiere de una contraseña.")
    private String password;

    // CascadeType.ALL: Borra por cascada.
    @OneToMany(mappedBy = "usuarioModel")
    @JsonIgnore
    private List<VentasModel> ventaModel;

    // CascadeType.ALL: Borra por cascada.
    @OneToMany(mappedBy = "usuarioModel")
    @JsonIgnore
    private List<PedidoModel> pedidoModel;


}
