package com.example.NoLimits.Multimedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    @Query("""
            SELECT u.id, u.nombre, u.apellidos, u.correo, u.telefono FROM UsuarioModel u
        """)
    List<Object[]> findUsuariosResumen();
    
    List<UsuarioModel> findByApellidos(String apellidos);

    
    List<UsuarioModel> findByNombreAndApellidos(String nombre, String apellidos);
    List<UsuarioModel> findByNombre(String nombreUsuario);
    List<UsuarioModel> findByCorreo(String correoUsuario);
    boolean existsByCorreo(String correo);

}
