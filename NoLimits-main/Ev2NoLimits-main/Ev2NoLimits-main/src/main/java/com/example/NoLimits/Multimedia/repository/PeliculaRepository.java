package com.example.NoLimits.Multimedia.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.PeliculaModel;

@Repository
public interface PeliculaRepository extends JpaRepository<PeliculaModel, Long>{

        @Query("""
            SELECT p.idPelicula, p.nombrePelicula, p.categoriaPelicula,
            p.plataformaPelicula, p.duracionPelicula, 
            p.descripcionPelicula, p.precioPelicula FROM PeliculaModel p
            """) 
    List<Object[]> findByPeliculaConBiblioteca();

    List<PeliculaModel> findByNombrePelicula(String nombrePelicula);
    List<PeliculaModel> findByCategoriaPelicula(String categoriaPelicula);
    List<PeliculaModel> findByDuracionPelicula(Integer duracionPelicula);
    List<PeliculaModel> findByDescripcionPelicula(String descripcionPelicula);
    List<PeliculaModel> findByPlataformaPelicula(String plataformaPelicula);
    List<PeliculaModel> findByPrecioPelicula(BigDecimal precioPelicula);
    
}
