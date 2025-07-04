package com.example.NoLimits.Multimedia.repository;

import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.model.VentasModel;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaModel, Long> {
    @Query("""
        SELECT dv.idDetalleVenta, dv.cantidad, dv.precioUnitario, dv.subtotal,
            dv.venta.id, dv.accesorio.id, dv.pelicula.id, dv.videojuego.id
        FROM DetalleVentaModel dv
    """)
    List<Object[]> findDetalleVentaResumen();

    List<DetalleVentaModel> findByIdDetalleVenta(long idDetalleVenta);

    List<DetalleVentaModel> findByCantidad(Integer cantidad);

    List<DetalleVentaModel> findByPrecioUnitario(Float precioUnitario);

    List<DetalleVentaModel> findBySubtotal(Float subtotal);

    // Este método encontrará todos los detalles que pertenezcan a una venta específica.
    List<DetalleVentaModel> findByVenta(VentasModel venta);

    void deleteByPelicula(PeliculaModel pelicula);

    void deleteByAccesorio(AccesorioModel accesorio);

    void deleteByVideojuego(VideojuegoModel videojuego);

    List<DetalleVentaModel> findByVenta_IdAndPelicula_IdPelicula(Long idVenta, Long idPelicula);


}
