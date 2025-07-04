package com.example.NoLimits.Multimedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.TipoProductoModel;

@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProductoModel, Long>{
    
    @Query("""
        SELECT tp.idTipoProducto, tp.nombreTipoProducto FROM TipoProductoModel tp
        """)
            List<Object[]> findByTipoProductoConBiblioteca();

            List<TipoProductoModel> findByNombreTipoProducto(String nombreTipoProducto);
            boolean existsByNombreTipoProducto(String nombreTipoProducto);

}
