package com.example.NoLimits.Multimedia.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;

// Define la interfaz como repositorio.
@Repository
// Extiende JpaRepository: Permite métodos como findAll(), etc.
public interface AccesorioRepository extends JpaRepository<AccesorioModel, Long> {

    // Query: Realizar consultas SQL.
    @Query("""
        SELECT a.idAccesorio, a.nombreAccesorio, 
            a.tipoAccesorio, a.marcaAccesorio, a.descripcionAccesorio, 
            a.precioAccesorio, a.stockAccesorio 
        FROM AccesorioModel a
    """)
    List<Object[]> findByAccesorioConBiblioteca();

    List<AccesorioModel> findByNombreAccesorio(String nombreAccesorio);
    List<AccesorioModel> findByTipoAccesorio(String tipoAccesorio);
    List<AccesorioModel> findByMarcaAccesorio(String marcaAccesorio);
    List<AccesorioModel> findByDescripcionAccesorio(String descripcionAccesorio);
    List<AccesorioModel> findByPrecioAccesorio(BigDecimal precioAccesorio);
    List<AccesorioModel> findByStockAccesorio(Integer stockAccesorio);

    void deleteByPelicula(PeliculaModel pelicula);
    
    void deleteByVideojuego(VideojuegoModel videojuego);


}
