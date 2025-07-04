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
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PedidoModel{

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NotNull(message = "Es necesario especificar una dirección de entrega.")
    private String direccionEntrega;

    @Column(length = 50, nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Es necesario que exista un usuario.")
    private UsuarioModel usuarioModel;

    // orphanRemoval: Elimina los detalles de los pedidos asociados de la lista del objeto PedidoModel.
    @OneToMany(mappedBy = "pedido", orphanRemoval = true)
    @JsonIgnore
    private List<DetallePedidoModel> detallePedidos;

    @ManyToOne
    @JoinColumn(name = "metodo_pago_id", nullable = false)
    @NotNull(message = "Debe especificarse el método de pago.")
    private MetodoPagoModel metodoPagoModel;
}
