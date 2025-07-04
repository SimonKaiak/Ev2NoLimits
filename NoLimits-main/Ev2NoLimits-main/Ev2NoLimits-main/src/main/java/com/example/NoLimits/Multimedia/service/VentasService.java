package com.example.NoLimits.Multimedia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.VentasModel;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.VentasRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentasService {

    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    public List<VentasModel> findAll(){
        return ventasRepository.findAll();
    }

    public VentasModel findById(long id) {
        // Usar findById y lanzar una excepción personalizada si no se encuentra.
        return ventasRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con id " + id));
    }

    public VentasModel save(VentasModel ventas){
        return ventasRepository.save(ventas);
    }

    public void deleteById(long id) {
        // 1.Busca la venta para asegurarse de que existe.
        VentasModel venta = ventasRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con id: " + id));

        // 2.Busca y elimina todos los detalles asociados a esta venta.
        List<DetalleVentaModel> detalles = detalleVentaRepository.findByVenta(venta);
        detalleVentaRepository.deleteAll(detalles); 

        ventasRepository.deleteById(id);
    }

    // crear este metodo para obtener ventas por metodo de pago
    // desde aqui los cambios
    public List<VentasModel> obtenerVentaPorMetodoPago(Long metodoPagoId) {
        // Llama al repositorio para obtener las ventas por el ID del método de pago.
        return ventasRepository.findAll().stream()
                .filter(venta -> venta.getMetodoPagoModel() != null
                        && venta.getMetodoPagoModel().getId().equals(metodoPagoId))
                .toList();
    }

    public List<VentasModel> obtenerVentaPorFechaCompra(java.time.LocalDate fechaCompra) {
        // Llama al repositorio para obtener las ventas por la fecha de compra.
        return ventasRepository.findByFechaCompra(fechaCompra);
    }

    public VentasModel obtenerVentaPorHoraCompra(java.time.LocalTime horaCompra) {
        // Llama al repositorio para obtener la venta por la hora de compra.
        return ventasRepository.findByHoraCompra(horaCompra);
    }

    public List<VentasModel> obtenerVentaPorUsuario(Long usuarioId) {
        // Llama al repositorio para obtener las ventas por el ID del usuario.
        return ventasRepository.findAll().stream()
                .filter(venta -> venta.getUsuarioModel() != null && venta.getUsuarioModel().getId().equals(usuarioId))
                .toList();
    }

    public VentasModel updateVentasModel(Long id, VentasModel ventasModel) {
        Optional<VentasModel> ventaOptional = ventasRepository.findById(id);
        if (ventaOptional.isPresent()) {
            VentasModel venta = ventaOptional.get();
            venta.setFechaCompra(ventasModel.getFechaCompra());
            venta.setHoraCompra(ventasModel.getHoraCompra());
            venta.setTotalVenta(ventasModel.getTotalVenta());
            venta.setMetodoPagoModel(ventasModel.getMetodoPagoModel());
            venta.setUsuarioModel(ventasModel.getUsuarioModel());
            return ventasRepository.save(venta);
        } else {
            return null;
        } // hasta aqui los cambios
    }

    public VentasModel patchVentasModel(Long id, VentasModel parcialVentasModel){
        Optional <VentasModel> ventaOptional = ventasRepository.findById(id);
        if (ventaOptional.isPresent()){
            VentasModel venta = ventaOptional.get();
            if(parcialVentasModel.getFechaCompra() != null){
                venta.setFechaCompra(parcialVentasModel.getFechaCompra());
            }
            if(parcialVentasModel.getHoraCompra() !=null){
                venta.setHoraCompra(parcialVentasModel.getHoraCompra());
            }
            if(parcialVentasModel.getTotalVenta() != null){
                venta.setTotalVenta(parcialVentasModel.getTotalVenta());
            }
            return  ventasRepository.save(venta);
            }else{
                return null;
            }
    }

    // Método que obtiene el Ventas con los datos.
    public List<Map<String, Object>> obtenerVentasConDatos(){
        // Existe en repository
        // Creamos una lista que guarde objetos con datos.
        List<Object[]> resultados = ventasRepository.findVentasResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        // Recorre la lista.
        for(Object[] fila : resultados){
            // HashMap: Similar a Map solo que no garantiza ningún orden.
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID: ", fila[0]);
            datos.put("Fecha compra: ", fila[1]);
            datos.put("Hora compra: ", fila[2]);
            datos.put("Total: ", fila[3]);
            datos.put("Método pago: ", fila[4]);
            datos.put("Usuario: ", fila[5]);

            // Agrega a la lista.
            lista.add(datos);
        }
        return lista;
    }
}
