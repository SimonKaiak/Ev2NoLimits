package com.example.NoLimits.Multimedia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.repository.AccesorioRepository;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.PeliculaRepository;
import com.example.NoLimits.Multimedia.repository.TipoProductoRepository;

import jakarta.transaction.Transactional;

// Define la clase como Servicio.
@Service
// Marca que un método o clase se ejecute dentro de una transacción de datos.
// Es decir: Que pasen por estos procesos.
@Transactional
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;
    
    @Autowired
    private TipoProductoRepository tipoProducto;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private AccesorioRepository accesorioRepository;

    public List<PeliculaModel> obtenerPeliculas() {
        return peliculaRepository.findAll();
    }

    public List<PeliculaModel> obtenerPeliculaPorNombre(String nombrePelicula) {
        return peliculaRepository.findByNombrePelicula(nombrePelicula);
    }

    public PeliculaModel obtenerPeliculaPorId(Long idPelicula) {
        return peliculaRepository.findById(idPelicula).orElse(null);
        
    }

    public List<PeliculaModel> obtenerPeliculaPorCategoria(String categoriaPelicula) {
        return peliculaRepository.findByCategoriaPelicula(categoriaPelicula);
    }

    public List<PeliculaModel> obtenerPeliculaPorPlataforma(String plataformaPelicula) {
        return peliculaRepository.findByPlataformaPelicula(plataformaPelicula);
    }

    public List<PeliculaModel> obtenerPeliculaPorPrecio(BigDecimal precioPelicula) {
        return peliculaRepository.findByPrecioPelicula(precioPelicula);
    }

    public List<Map<String, Object>> obtenerPeliculasConNombres() {
        // Existe en repository
        List<Object[]> resultados = peliculaRepository.findByPeliculaConBiblioteca();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            // HashMap:
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID", fila[0]);
            datos.put("Nombre", fila[1]);
            datos.put("Categoría", fila[2]);
            datos.put("Plataforma", fila[3]);
            datos.put("Duración", fila[4]);
            datos.put("Descripción", fila[5]);
            datos.put("Precio", fila[6]);
            lista.add(datos);
        }
        return lista;
    }

    public PeliculaModel guardarPelicula(PeliculaModel peliculaModel) {
        return (PeliculaModel) peliculaRepository.save(peliculaModel);
    }

    public PeliculaModel actualizarPelicula(Long idPelicula, PeliculaModel peliculaModel) {
        PeliculaModel peliculaExistente = peliculaRepository.findById(idPelicula).orElse(null);
        if (peliculaExistente != null) {
            peliculaExistente.setNombrePelicula(peliculaModel.getNombrePelicula());
            peliculaExistente.setCategoriaPelicula(peliculaModel.getCategoriaPelicula());
            peliculaExistente.setPlataformaPelicula(peliculaModel.getPlataformaPelicula());
            peliculaExistente.setDuracionPelicula(peliculaModel.getDuracionPelicula());
            peliculaExistente.setDescripcionPelicula(peliculaModel.getDescripcionPelicula());
            peliculaExistente.setPrecioPelicula(peliculaModel.getPrecioPelicula());
            return peliculaRepository.save(peliculaExistente);
        } else {
            return null;
        }
    }

    public PeliculaModel actualizarPeliculaParcial(Long idPelicula, PeliculaModel peliculaParcial) {
        PeliculaModel peliculaExistente = peliculaRepository.findById(idPelicula).orElse(null);
        if (peliculaExistente != null) {
            if (peliculaParcial.getNombrePelicula() != null) {
                peliculaExistente.setNombrePelicula(peliculaParcial.getNombrePelicula());
            }
            if (peliculaParcial.getCategoriaPelicula() != null) {
                peliculaExistente.setCategoriaPelicula(peliculaParcial.getCategoriaPelicula());
            }
            if (peliculaParcial.getPlataformaPelicula() != null) {
                peliculaExistente.setPlataformaPelicula(peliculaParcial.getPlataformaPelicula());
            }
            if (peliculaParcial.getDuracionPelicula() != null) {
                peliculaExistente.setDuracionPelicula(peliculaParcial.getDuracionPelicula());
            }
            if (peliculaParcial.getDescripcionPelicula() != null) {
                peliculaExistente.setDescripcionPelicula(peliculaParcial.getDescripcionPelicula());
            }
            if (peliculaParcial.getPrecioPelicula() != null) {
                peliculaExistente.setPrecioPelicula(peliculaParcial.getPrecioPelicula());
            }
            return peliculaRepository.save(peliculaExistente);
        } else {
            return null;
        }
    }

    public void eliminarPeliculaPorId(Long idPelicula) {
        // 1. Buscar el pelicula por ID. Si no existe, lanzar excepción personalizada.
        PeliculaModel pelicula = peliculaRepository.findById(idPelicula)
            .orElseThrow(() -> new RecursoNoEncontradoException("Película no encontrada con id: " + idPelicula));

        // Estas eliminaciones se ejecutan solo si hay registros. Si no, no pasa nada malo.
        detalleVentaRepository.deleteByPelicula(pelicula);
        detallePedidoRepository.deleteByPelicula(pelicula);
        accesorioRepository.deleteByPelicula(pelicula);

        // Elimina la película.
        peliculaRepository.delete(pelicula);
    }
}
