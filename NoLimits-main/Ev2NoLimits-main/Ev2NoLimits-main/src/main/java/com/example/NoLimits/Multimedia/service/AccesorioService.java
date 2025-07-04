package com.example.NoLimits.Multimedia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;
import com.example.NoLimits.Multimedia.repository.AccesorioRepository;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.PeliculaRepository;
import com.example.NoLimits.Multimedia.repository.VideojuegoRepository;

import jakarta.transaction.Transactional;

// Define la clase como Servicio.
@Service
// Marca que un método o clase se ejecute dentro de una transacción de datos. 
// Es decir: Que pasen por estos procesos.
@Transactional

public class AccesorioService {

    // Herencia.
    @Autowired
    private AccesorioRepository accesorioRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    // Obtener todos.
    public List<AccesorioModel> obtenerAccesorios(){
        return accesorioRepository.findAll();
    }

    // Obtener por nombre.
    public List<AccesorioModel> obtenerAccesoriosPorNombre(String nombreAccesorio){
        return accesorioRepository.findByNombreAccesorio(nombreAccesorio);
    }

    // Obtener por id.
    public Optional<AccesorioModel> obtenerAccesorioPorId(Long idAccesorio){
        return accesorioRepository.findById(idAccesorio);
    }

    // Obtener por tipo.
    public List<AccesorioModel> obtenerAccesoriosPorTipo(String tipoAccesorio) {
        return accesorioRepository.findByTipoAccesorio(tipoAccesorio);
    }

    // Obtener por marca.
    public List<AccesorioModel> obtenerAccesoriosPorMarca(String marcaAccesorio) {
        return accesorioRepository.findByMarcaAccesorio(marcaAccesorio);
    }

    // Llamar por precio.
    public List<AccesorioModel> obtenerAccesorioPorPrecio(BigDecimal precioAccesorio){
        return accesorioRepository.findByPrecioAccesorio(precioAccesorio);
    }
    // Llamar por Stock.
    public List<AccesorioModel> obtenerAccesorioPorStock(Integer stockAccesorio){
        return accesorioRepository.findByStockAccesorio(stockAccesorio);
    }

    // Guardar accesorio.
    public AccesorioModel guardarAccesorio(AccesorioModel accesorioModel) {
    // Validar existencia de película
    PeliculaModel pelicula = peliculaRepository.findById(accesorioModel.getPelicula().getIdPelicula())
        .orElseThrow(() -> new RecursoNoEncontradoException("Película no encontrada"));

    // Validar existencia de videojuego
    VideojuegoModel videojuego = videojuegoRepository.findById(accesorioModel.getVideojuego().getIdVideojuego())
        .orElseThrow(() -> new RecursoNoEncontradoException("Videojuego no encontrado"));

        // Asociar objetos válidos
        accesorioModel.setPelicula(pelicula);
        accesorioModel.setVideojuego(videojuego);

    // Guardar y retornar accesorio
        return accesorioRepository.save(accesorioModel);
    }

    // Eliminar accesorio.
    public void eliminarAccesorioPorId(Long idAccesorio) {
        // 1. Buscar el accesorio por ID. Si no existe, lanza excepción personalizada.
        AccesorioModel accesorio = accesorioRepository.findById(idAccesorio)
            .orElseThrow(() -> new RecursoNoEncontradoException("Accesorio no encontrado con ID: " + idAccesorio));

        // Primero eliminamos los detalles asociados al accesorio
        detalleVentaRepository.deleteByAccesorio(accesorio);
        detallePedidoRepository.deleteByAccesorio(accesorio);

        // 2. Eliminar el accesorio encontrado.
        accesorioRepository.delete(accesorio);
    }

    // PUT
    // Actualizar accesorio.
    public AccesorioModel actualizarAccesorio(Long idAccesorio, AccesorioModel accesorioModel){
        AccesorioModel accesorioExistente = accesorioRepository.findById(idAccesorio).orElse(null);
    if(accesorioExistente != null){
        accesorioExistente.setNombreAccesorio(accesorioModel.getNombreAccesorio());
        accesorioExistente.setTipoAccesorio(accesorioModel.getTipoAccesorio());
        accesorioExistente.setMarcaAccesorio(accesorioModel.getMarcaAccesorio());
        accesorioExistente.setDescripcionAccesorio(accesorioModel.getDescripcionAccesorio());
        accesorioExistente.setPrecioAccesorio(accesorioModel.getPrecioAccesorio());
        accesorioExistente.setStockAccesorio(accesorioModel.getStockAccesorio());
        return accesorioRepository.save(accesorioExistente);
    } else{
        return null;
    }
    }

    // PATCH
    // Actualizar cosas específicas del accesorio.
    public AccesorioModel actualizarAccesorioParcial(Long idAccesorio, AccesorioModel accesorioParcial) {
        AccesorioModel accesorioExistente = accesorioRepository.findById(idAccesorio).orElse(null);
        if(accesorioExistente != null){
            if(accesorioParcial.getNombreAccesorio() != null){
                accesorioExistente.setNombreAccesorio(accesorioParcial.getNombreAccesorio());
        }
            if(accesorioParcial.getTipoAccesorio() != null){
                accesorioExistente.setTipoAccesorio(accesorioParcial.getTipoAccesorio()); 
            }
            if(accesorioParcial.getMarcaAccesorio() != null){
                accesorioExistente.setMarcaAccesorio(accesorioParcial.getMarcaAccesorio());
            }
            if(accesorioParcial.getDescripcionAccesorio() != null){
                accesorioExistente.setDescripcionAccesorio(accesorioParcial.getDescripcionAccesorio());
            }
            if(accesorioParcial.getPrecioAccesorio() != null){
                accesorioExistente.setPrecioAccesorio(accesorioParcial.getPrecioAccesorio());
            }
            if(accesorioParcial.getStockAccesorio() != null){
                accesorioExistente.setStockAccesorio(accesorioParcial.getStockAccesorio());
            }
        return accesorioRepository.save(accesorioExistente);
    } else{
        return null;
        }
    }

    // Método que obtiene el accesorio con los datos.
    public List<Map<String, Object>> obtenerAccesoriosConDatos(){
        // Existe en repository
        // Creamos una lista que guarde objetos con datos.
        List<Object[]> resultados = accesorioRepository.findByAccesorioConBiblioteca();
        List<Map<String, Object>> lista = new ArrayList<>();

        // Recorre la lista.
        for(Object[] fila : resultados){
            // HashMap: Similar a Map solo que no garantiza ningún orden.
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID Accesorio", fila[0]);
            datos.put("Nombre Accesorio", fila[1]);
            datos.put("Tipo Accesorio", fila[2]);
            datos.put("Marca Accesorio", fila[3]);
            datos.put("Descripción Accesorio", fila[4]);
            datos.put("Precio Accesorio", fila[5]);
            datos.put("Stock Accesorio", fila[6]);
            // Agrega a la lista.
            lista.add(datos);
        }
        return lista;
    }
}
