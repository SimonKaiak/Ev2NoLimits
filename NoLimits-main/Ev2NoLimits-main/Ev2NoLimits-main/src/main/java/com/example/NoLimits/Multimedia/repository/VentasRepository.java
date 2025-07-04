package com.example.NoLimits.Multimedia.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.VentasModel;

@Repository
public interface VentasRepository extends JpaRepository<VentasModel, Long>{

    @Query("""
        SELECT v.id, v.fechaCompra, v.horaCompra, v.totalVenta, m.nombre, u.nombre
        FROM VentasModel v
        JOIN v.metodoPagoModel m
        JOIN v.usuarioModel u
    """)
    List<Object[]> findVentasResumen();

    List<VentasModel> findByFechaCompra(LocalDate FechaCompra);

    VentasModel findByHoraCompra(LocalTime HoraCompra);

    VentasModel findById(Integer id);

    List<VentasModel> findByUsuarioModel_IdAndMetodoPagoModel_Id(Long idUsuario, Long idMetodoPago);

}
