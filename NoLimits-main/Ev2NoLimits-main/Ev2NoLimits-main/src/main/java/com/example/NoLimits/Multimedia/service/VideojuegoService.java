package com.example.NoLimits.Multimedia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;
import com.example.NoLimits.Multimedia.repository.AccesorioRepository;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.VideojuegoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class VideojuegoService {

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private AccesorioRepository accesorioRepository;

    public List<VideojuegoModel> obtenerVideojuegos(){
        return videojuegoRepository.findAll();
    }

    public List<VideojuegoModel> obtenerVideojuegoPorNombre(String nombreVideojuego){
        return videojuegoRepository.findByNombreVideojuego(nombreVideojuego);
    }

    public VideojuegoModel obtenerVideojuegoPorId(Long idVideojuego){
        return videojuegoRepository.findById(idVideojuego).orElse(null);
    }

    public List<VideojuegoModel> obtenerVideojuegoPorCategoria(String categoriaVideojuego){
        return videojuegoRepository.findByCategoriaVideojuego(categoriaVideojuego);
    }

    public List<VideojuegoModel> obtenerVideojuegoPorPlataforma(String plataformaVideojuego){
        return videojuegoRepository.findByPlataformaVideojuego(plataformaVideojuego);
    }

    public List<VideojuegoModel> obtenerVideojuegoPorDesarrollador(String desarrolladorVideojuego){
        return videojuegoRepository.findByDesarrolladorVideojuego(desarrolladorVideojuego);
    }

    public List<VideojuegoModel> obtenerVideojuegoPorPrecio(BigDecimal precioVideojuego){
        return videojuegoRepository.findByPrecioVideojuego(precioVideojuego);
    }

    public List<Map<String, Object>> obtenerVideojuegosConNombres(){
        // Existe en repository
        List<Object[]> resultados = videojuegoRepository.findByVideojuegoConBiblioteca();
        List<Map<String, Object>> lista = new ArrayList<>();

        for(Object[] fila : resultados){
            // HashMap: 
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID", fila[0]);
            datos.put("Nombre", fila[1]);
            datos.put("Categoría", fila[2]);
            datos.put("Plataforma", fila[3]);
            datos.put("Desesarrollador", fila[4]);
            datos.put("Descripción", fila[5]);
            datos.put("Precio", fila[6]);
            lista.add(datos);
        }
        return lista;
    }

    public VideojuegoModel guardarVideojuego(VideojuegoModel videojuegoModel){
        return(VideojuegoModel)videojuegoRepository.save(videojuegoModel);
    }

    public VideojuegoModel actualizarVideojuego(Long idVideojuego, VideojuegoModel videojuegoModel){
        VideojuegoModel videojuegoExistente = videojuegoRepository.findById(idVideojuego).orElse(null);
    if(videojuegoExistente != null){
        videojuegoExistente.setNombreVideojuego(videojuegoModel.getNombreVideojuego());
        videojuegoExistente.setCategoriaVideojuego(videojuegoModel.getCategoriaVideojuego());
        videojuegoExistente.setPlataformaVideojuego(videojuegoModel.getPlataformaVideojuego());
        videojuegoExistente.setDesarrolladorVideojuego(videojuegoModel.getDesarrolladorVideojuego());
        videojuegoExistente.setDescripcionVideojuego(videojuegoModel.getDescripcionVideojuego());
        videojuegoExistente.setPrecioVideojuego(videojuegoModel.getPrecioVideojuego());
        return videojuegoRepository.save(videojuegoExistente);
    } else{
        return null;
    }
}

public VideojuegoModel actualizarVideojuegoParcial(Long idVideojuego, VideojuegoModel videojuegoParcial) {
    VideojuegoModel videojuegoExistente = videojuegoRepository.findById(idVideojuego).orElse(null);
    if(videojuegoExistente != null){
        if(videojuegoParcial.getNombreVideojuego() != null){
            videojuegoExistente.setNombreVideojuego(videojuegoParcial.getNombreVideojuego());
    }
        if(videojuegoParcial.getCategoriaVideojuego() != null){
            videojuegoExistente.setCategoriaVideojuego(videojuegoParcial.getCategoriaVideojuego()); 
        }
        if(videojuegoParcial.getPlataformaVideojuego() != null){
            videojuegoExistente.setPlataformaVideojuego(videojuegoParcial.getPlataformaVideojuego()); 
        }
        if(videojuegoParcial.getDesarrolladorVideojuego() != null){
           videojuegoExistente.setDesarrolladorVideojuego(videojuegoParcial.getDesarrolladorVideojuego());
        }
        if(videojuegoParcial.getDescripcionVideojuego() != null){
            videojuegoExistente.setDescripcionVideojuego(videojuegoParcial.getDescripcionVideojuego());
        }
        if(videojuegoParcial.getPrecioVideojuego() != null){
            videojuegoExistente.setPrecioVideojuego(videojuegoParcial.getPrecioVideojuego()); 
        }
        return videojuegoRepository.save(videojuegoExistente);
    } else{
        return null;
    }
}

    public void eliminarVideojuegoPorId(Long idVideojuego) {
    // 1. Buscar el videojuego por ID. Si no existe, lanzar excepción personalizada.
    VideojuegoModel videojuego = videojuegoRepository.findById(idVideojuego)
        .orElseThrow(() -> new RecursoNoEncontradoException("Videojuego no encontrado con ID: " + idVideojuego));

    // 2. Eliminar detalles asociados
    detalleVentaRepository.deleteByVideojuego(videojuego);
    detallePedidoRepository.deleteByVideojuego(videojuego);
    accesorioRepository.deleteByVideojuego(videojuego); // si el accesorio está vinculado al videojuego

    // 3. Eliminar el videojuego
    videojuegoRepository.delete(videojuego);
}
}
