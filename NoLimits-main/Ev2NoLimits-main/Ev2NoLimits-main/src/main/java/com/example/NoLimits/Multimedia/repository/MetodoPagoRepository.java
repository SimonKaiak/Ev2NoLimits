package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPagoModel, Long> {
    @Query("""
            SELECT mp.id, mp.nombre, mp.ventas FROM MetodoPagoModel mp
            """)
    List<Object[]> findMetodoPagoResumen();

    // Busca un metodo de pago por su nombre
    Optional<MetodoPagoModel> findByNombre(String nombre);
    boolean existsByNombre(String nombre);

}
