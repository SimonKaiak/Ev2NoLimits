package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia.model.DetallePedidoModel;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
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
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    // Obtener todos los detalles de pedido
    public List<DetallePedidoModel> findAll() {
        return detallePedidoRepository.findAll();
    }

    // Obtener un detalle de pedido por su ID
    public Optional<DetallePedidoModel> findById(Long id) {
        return detallePedidoRepository.findById(id);
    }

    // Guardar un nuevo detalle de pedido
    public DetallePedidoModel save(DetallePedidoModel detallePedidoModel) {
        return detallePedidoRepository.save(detallePedidoModel);
    }

    // Eliminar un detalle de pedido por su ID
    public void deleteById(Long id) {
        detallePedidoRepository.deleteById(id);
    }

    // Método para actualizar un detalle de pedido completo (PUT)
    public DetallePedidoModel update(Long id, DetallePedidoModel detallePedidoDetalles) {
        Optional<DetallePedidoModel> optionalDetalle = detallePedidoRepository.findById(id);
        if (optionalDetalle.isPresent()) {
            DetallePedidoModel detalleExistente = optionalDetalle.get();
            detalleExistente.setPedido(detallePedidoDetalles.getPedido());
            detalleExistente.setAccesorio(detallePedidoDetalles.getAccesorio());
            detalleExistente.setPelicula(detallePedidoDetalles.getPelicula());
            detalleExistente.setVideojuego(detallePedidoDetalles.getVideojuego());
            detalleExistente.setCantidad(detallePedidoDetalles.getCantidad());
            detalleExistente.setPrecioUnitario(detallePedidoDetalles.getPrecioUnitario());
            detalleExistente.setSubtotal(detallePedidoDetalles.getSubtotal());
            return detallePedidoRepository.save(detalleExistente);
        }
        return null;
    }

    // Método para actualizar parcialmente un detalle de pedido (PATCH)
    public DetallePedidoModel patch(Long id, DetallePedidoModel detallePedidoPartial) {
        Optional<DetallePedidoModel> optionalDetalle = detallePedidoRepository.findById(id);
        if (optionalDetalle.isPresent()) {
            DetallePedidoModel detalleExistente = optionalDetalle.get();
            if (detallePedidoPartial.getCantidad() != null) {
                detalleExistente.setCantidad(detallePedidoPartial.getCantidad());
            }
            if (detallePedidoPartial.getPrecioUnitario() != null) {
                detalleExistente.setPrecioUnitario(detallePedidoPartial.getPrecioUnitario());
            }
             if (detallePedidoPartial.getCantidad() != null || detallePedidoPartial.getPrecioUnitario() != null) {
                Float newSubtotal = detalleExistente.getCantidad() * detalleExistente.getPrecioUnitario();
                detalleExistente.setSubtotal(newSubtotal);
            }
            return detallePedidoRepository.save(detalleExistente);
        }
        return null;
    }

    // Método que obtiene el DetallePedido con los datos.
    public List<Map<String, Object>> obtenerDetallePedidoConDatos(){
        // Existe en repository
        // Creamos una lista que guarde objetos con datos.
        List<Object[]> resultados = detallePedidoRepository.findByDetallePedidoResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        // Recorre la lista.
        for(Object[] fila : resultados){
            // HashMap: Similar a Map solo que no garantiza ningún orden.
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID: ", fila[0]);
            datos.put("Cantidad: ", fila[1]);
            datos.put("Precio Unitario: ", fila[2]);
            datos.put("Subtotal: ", fila[3]);
            datos.put("Pedido: ", fila[4]);
            datos.put("Accesorio: ", fila[5]);
            datos.put("Pelicula: ", fila[6]);
            datos.put("Videojuego: ", fila[7]);

            // Agrega a la lista.
            lista.add(datos);
        }
        return lista;
    }

    public DetallePedidoModel patchDetallePedido(long l, DetallePedidoModel patchData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'patchDetallePedido'");
    }


}
