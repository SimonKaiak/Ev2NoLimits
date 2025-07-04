package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    // Obtener todos los detalles de venta
    public List<DetalleVentaModel> findAll() {
        return detalleVentaRepository.findAll();
    }

    // Obtener un detalle de venta por su ID
    public Optional<DetalleVentaModel> findById(Long id) {
        return detalleVentaRepository.findById(id);
    }

    // Guardar un nuevo detalle de venta
    public DetalleVentaModel save(DetalleVentaModel detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    // Eliminar un detalle de venta por su ID
    public void deleteById(Long id) {
        detalleVentaRepository.deleteById(id);

    }

    // Método para actualizar un detalle de venta completo (PUT)
    public DetalleVentaModel update(Long id, DetalleVentaModel detalleVentaDetails) {
        Optional<DetalleVentaModel> optionalDetalle = detalleVentaRepository.findById(id);
        if (optionalDetalle.isPresent()) {
            DetalleVentaModel detalleExistente = optionalDetalle.get();
            // Asigna todos los nuevos valores
            detalleExistente.setVenta(detalleVentaDetails.getVenta());
            detalleExistente.setAccesorio(detalleVentaDetails.getAccesorio());
            detalleExistente.setPelicula(detalleVentaDetails.getPelicula());
            detalleExistente.setVideojuego(detalleVentaDetails.getVideojuego());
            detalleExistente.setCantidad(detalleVentaDetails.getCantidad());
            detalleExistente.setPrecioUnitario(detalleVentaDetails.getPrecioUnitario());
            detalleExistente.setSubtotal(detalleVentaDetails.getSubtotal());
            return detalleVentaRepository.save(detalleExistente);
        }
        return null;
    }

    // Método que obtiene el DetalleVenta con los datos.
    public List<Map<String, Object>> obtenerDetalleVentaConDatos() {
        // Existe en repository
        // Creamos una lista que guarde objetos con datos.
        List<Object[]> resultados = detalleVentaRepository.findDetalleVentaResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        // Recorre la lista.
        for (Object[] fila : resultados) {
            // HashMap: Similar a Map solo que no garantiza ningún orden.
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID: ", fila[0]);
            datos.put("Cantidad: ", fila[1]);
            datos.put("Precio Unitario: ", fila[2]);
            datos.put("Subtotal: ", fila[3]);
            datos.put("Venta: ", fila[4]);
            datos.put("Accesorio: ", fila[5]);
            datos.put("Pelicula: ", fila[6]);
            datos.put("Videojuego: ", fila[7]);

            // Agrega a la lista.
            lista.add(datos);
        }
        return lista;
    }

    // Método para actualizar parcialmente un detalle de venta (PATCH)
    public DetalleVentaModel patchDetalleVentaModel(Long id, DetalleVentaModel detalleVentaParcial) {
        Optional<DetalleVentaModel> optionalDetalle = detalleVentaRepository.findById(id);
        if (optionalDetalle.isPresent()) {
            DetalleVentaModel detalleExistente = optionalDetalle.get();
            if (detalleVentaParcial.getCantidad() != null) {
                detalleExistente.setCantidad(detalleVentaParcial.getCantidad());
            }
            if (detalleVentaParcial.getPrecioUnitario() != null) {
                detalleExistente.setPrecioUnitario(detalleVentaParcial.getPrecioUnitario());
            }
            // Actualizar el subtotal si cambia la cantidad o el precio
            if (detalleVentaParcial.getCantidad() != null || detalleVentaParcial.getPrecioUnitario() != null) {
                Float newSubtotal = detalleExistente.getCantidad() * detalleExistente.getPrecioUnitario();
                detalleExistente.setSubtotal(newSubtotal);
            }
            return detalleVentaRepository.save(detalleExistente);
        }
        return null;
    }
}
