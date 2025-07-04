package com.example.NoLimits.Multimedia.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    @NotNull(message = "La venta requiere de una fecha de compra.")
    private LocalDate fechaCompra;

    @Column(nullable = false)
    @NotNull(message = "La venta requiere de una hora de compra.")
    private LocalTime horaCompra;

    @Column(nullable = false)
    @NotNull(message = "La venta requiere de un valor total.")
    private Integer totalVenta;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago") // FK en la tabla de ventas
    private MetodoPagoModel metodoPagoModel;

    @ManyToOne
    @JoinColumn(name = "usuario_id") // FK en la tabla de usuario
    private UsuarioModel usuarioModel;

    @OneToMany(mappedBy = "venta", orphanRemoval = true)
    private List<DetalleVentaModel> detalleVenta;
}
