package com.example.NoLimits.Multimedia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //ObtenerUsuarios
    public List<UsuarioModel> findAll() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel findById(long id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
    }
    
    public UsuarioModel save(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deletebyId(long id) {
        //verifica si el usuario existe para poder lanzar un error 404 claro.
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
        // Si existe, JPA se encargará de la cascada automáticamente.
        usuarioRepository.deleteById(id);
    }

    // Llamar por nombre.
    public List<UsuarioModel> obtenerUsuarioPorNombre(String nombreUsuario){
        return usuarioRepository.findByNombre(nombreUsuario);
    }

    // Llamar por correo.
    public List<UsuarioModel> obtenerUsuarioPorCorreo(String correoUsuario){
        return usuarioRepository.findByCorreo(correoUsuario);
    }

    // Put
    // Actualizar TipoProducto.
    public UsuarioModel actualizarUsuario(Long id, UsuarioModel usuarioModel){
        UsuarioModel usuarioExistente = usuarioRepository.findById(id).orElse(null);
    if(usuarioExistente != null){
        usuarioExistente.setNombre(usuarioModel.getNombre());
        usuarioExistente.setApellidos(usuarioModel.getApellidos());
        usuarioExistente.setCorreo(usuarioModel.getCorreo());
        usuarioExistente.setTelefono(usuarioModel.getTelefono());
        usuarioExistente.setPassword(usuarioModel.getPassword());
        return usuarioRepository.save(usuarioExistente);
    } else{
        return null;
    }
}
    
    public UsuarioModel patchUsuarioModel(long id, UsuarioModel usuario) {
        Optional<UsuarioModel> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            UsuarioModel existingUsuario = optionalUsuario.get();
            if (usuario.getNombre() != null) {
                existingUsuario.setNombre(usuario.getNombre());
            }
            if (usuario.getApellidos() != null) {
                existingUsuario.setApellidos(usuario.getApellidos());
            }
            if (usuario.getCorreo() != null) {
                existingUsuario.setCorreo(usuario.getCorreo());
            }
            if (usuario.getTelefono() != null) {
                existingUsuario.setTelefono(usuario.getTelefono());
            }
            if (usuario.getPassword() != null) {
                existingUsuario.setPassword(usuario.getPassword());
            }
            return usuarioRepository.save(existingUsuario);
        } else {
            return null; // or throw an exception
        }
    }

    public UsuarioModel actualizarUsuario(long idUsuario, UsuarioModel usuarioModel) {
        UsuarioModel usuarioExistente = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuarioExistente != null) {
            usuarioExistente.setNombre(usuarioModel.getNombre());
            usuarioExistente.setApellidos(usuarioModel.getApellidos());
            usuarioExistente.setCorreo(usuarioModel.getCorreo());
            usuarioExistente.setTelefono(usuarioModel.getTelefono());
            usuarioExistente.setPassword(usuarioModel.getPassword());
            return usuarioRepository.save(usuarioExistente);
        } else {
            return null;
        }
    }

    // Método que obtiene el Usuarios con los datos.
    public List<Map<String, Object>> obtenerUsuariosConDatos(){
        // Existe en repository
        // Creamos una lista que guarde objetos con datos.
        List<Object[]> resultados = usuarioRepository.findUsuariosResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        // Recorre la lista.
        for(Object[] fila : resultados){
            // HashMap: Similar a Map solo que no garantiza ningún orden.
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID", fila[0]);
            datos.put("Nombre", fila[1]);
            datos.put("Apellidos", fila[2]);
            datos.put("Correo", fila[3]);
            datos.put("Teléfono", fila[4]);

            // Agrega a la lista.
            lista.add(datos);
        }
        return lista;
    }
}
