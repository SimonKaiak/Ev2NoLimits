package com.example.NoLimits.Multimedia.repository;

import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.DetallePedidoModel;
import com.example.NoLimits.Multimedia.model.PedidoModel;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoModel, Long> {
    @Query("""
            SELECT dp.idDetallePedido, dp.cantidad, dp.precioUnitario, dp.subtotal FROM DetallePedidoModel dp
            """)
    List<Object[]> findByDetallePedidoResumen();

    List<DetallePedidoModel> findByCantidad(Integer cantidad);

    List<DetallePedidoModel> findByPrecioUnitario(Float precioUnitario);

    List<DetallePedidoModel> findBySubtotal(Float subtotal);

    // Método para encontrar detalles por pedido.
    List<DetallePedidoModel> findByPedido(PedidoModel pedido);

    void deleteByPelicula(PeliculaModel pelicula);

    void deleteByAccesorio(AccesorioModel accesorio);

    void deleteByVideojuego(VideojuegoModel videojuego);

}
