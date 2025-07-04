package com.example.NoLimits.Multimedia.repository;

import com.example.NoLimits.Multimedia.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Long>{

    @Query("""
        SELECT ped.id, ped.direccionEntrega, ped.estado, ped.usuarioModel
        FROM PedidoModel ped
    """)
    List<Object[]> findByPedidosResumen();

    List<PedidoModel> findByDireccionEntrega(String direccionEntrega);
    List<PedidoModel> findByEstado(String estado); // Método para encontrar pedidos por estado
    List<PedidoModel> findByUsuarioModel_IdAndMetodoPagoModel_Id(Long idUsuario, Long idMetodo);

}