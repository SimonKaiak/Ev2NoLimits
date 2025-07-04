package com.example.NoLimits.Multimedia.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.VideojuegoModel;

@Repository
public interface VideojuegoRepository extends JpaRepository<VideojuegoModel, Long>{

        @Query("""
            SELECT v.idVideojuego, v.nombreVideojuego, v.categoriaVideojuego,
            v.plataformaVideojuego, v.desarrolladorVideojuego, 
            v.descripcionVideojuego, v.precioVideojuego FROM VideojuegoModel v
            """) 
    List<Object[]> findByVideojuegoConBiblioteca();

    List<VideojuegoModel> findByNombreVideojuego(String nombreVideojuego);
    List<VideojuegoModel> findByCategoriaVideojuego(String categoriaVideojuego);
    List<VideojuegoModel> findByPlataformaVideojuego(String plataformaVideojuego);
    List<VideojuegoModel> findByDesarrolladorVideojuego(String desarrolladorVideojuego);
    List<VideojuegoModel> findByDescripcionVideojuego(String descripcionVideojuego);
    List<VideojuegoModel> findByPrecioVideojuego(BigDecimal precioVideojuego);
    List<VideojuegoModel> findByNombreVideojuegoAndPlataformaVideojuego(String nombre, String plataforma);


}
