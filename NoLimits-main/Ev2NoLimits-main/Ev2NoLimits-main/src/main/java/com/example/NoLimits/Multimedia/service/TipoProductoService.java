package com.example.NoLimits.Multimedia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.TipoProductoModel;
import com.example.NoLimits.Multimedia.repository.AccesorioRepository;
import com.example.NoLimits.Multimedia.repository.PeliculaRepository;
import com.example.NoLimits.Multimedia.repository.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.VideojuegoRepository;

import jakarta.transaction.Transactional;

// Define la clase como Servicio.
@Service
// Marca que un método o clase se ejecute dentro de una transacción de datos. Es decir: Que pasen por estos procesos.
@Transactional
public class TipoProductoService {

    // Herencia.
    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private AccesorioRepository accesorioRepository;

    // Obtener todos.
    public List<TipoProductoModel> obtenerTiposProductos(){
        return tipoProductoRepository.findAll();
    }

    // Obtener por id
    public TipoProductoModel obtenerTipoProductoPorId(Long idTipoProducto){
        return tipoProductoRepository.findById(idTipoProducto).orElse(null);
    }

    // Obtener por nombre.
    public List<TipoProductoModel> obtenerTipoProductoPorNombre(String nombreTipoProducto){
        return tipoProductoRepository.findByNombreTipoProducto(nombreTipoProducto);
    }

    // Guardar TipoProducto.
    public TipoProductoModel guardarTipoProducto(TipoProductoModel tipoProductoModel) {
            return tipoProductoRepository.save(tipoProductoModel);
    }

    // Actualizar TipoProducto.
    public TipoProductoModel actualizarTipoProducto(Long idTipoProducto, TipoProductoModel tipoProductoModel){
        TipoProductoModel tipoProductoExistente = tipoProductoRepository.findById(idTipoProducto).orElse(null);
    if(tipoProductoExistente != null){
        tipoProductoExistente.setNombreTipoProducto(tipoProductoModel.getNombreTipoProducto());
        return tipoProductoRepository.save(tipoProductoExistente);
    } else{
        return null;
    }
}     

    // Actualizar TipoProducto Parcial.
    public TipoProductoModel actualizarTipoProductoParcial(Long idTipoProducto, TipoProductoModel tipoProductoParcial) {
        TipoProductoModel tipoProductoExistente = tipoProductoRepository.findById(idTipoProducto).orElse(null);
    if(tipoProductoExistente != null){
        if(tipoProductoParcial.getNombreTipoProducto() != null){
            tipoProductoExistente.setNombreTipoProducto(tipoProductoParcial.getNombreTipoProducto());
    }
        return tipoProductoRepository.save(tipoProductoExistente);
    } else{
        return null;
    }
}

    // Eliminar TipoProducto.
    public void eliminarTipoProducto(Long idTipoProducto){
        // Verifica si existe el TipoProducto con el ID proporcionado.
        TipoProductoModel tipoProducto = tipoProductoRepository.findById(idTipoProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el tipo de producto con ID: " + idTipoProducto));
        // Rompemos la relación con las entidades hijas.
        // sobre cada lista de productos asociados y les quitamos la referencia al padre.
        // Esto evita el error "deleted entity passed to persist".
        tipoProducto.getVideojuegos().forEach(videojuego -> {
        videojuego.setTipoProducto(null);
            videojuegoRepository.save(videojuego);
        });
        tipoProducto.getPeliculas().forEach(pelicula -> {
            pelicula.setTipoProducto(null);
            peliculaRepository.save(pelicula);
        });
        tipoProducto.getAccesorios().forEach(accesorio -> {
            accesorio.setTipoProducto(null);
            accesorioRepository.save(accesorio);
        });

        tipoProductoRepository.delete(tipoProducto);
    }

    // Método que obtiene el TipoProducto con los datos.
    public List<Map<String, Object>> obtenerTipoProductoConNombres(){
        // Existe en repository
        // Creamos una lista que guarde objetos con datos.
        List<Object[]> resultados = tipoProductoRepository.findByTipoProductoConBiblioteca();
        List<Map<String, Object>> lista = new ArrayList<>();

        // Recorre la lista.
        for(Object[] fila : resultados){
            // HashMap: Similar a Map solo que no garantiza ningún orden.
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID: ", fila[0]);
            datos.put("Nombre: ", fila[1]);
            // Agrega a la lista.
            lista.add(datos);
        }
        return lista;
    }
}
