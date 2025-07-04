package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.DetallePedidoModel;
import com.example.NoLimits.Multimedia.model.PedidoModel;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
import com.example.NoLimits.Multimedia.repository.PedidoRepository;
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
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    //Obtener todos los pedidos
    public List<PedidoModel> ObtenerTodosLosPedidos() { // Método para obtener todos los pedidos
        return pedidoRepository.findAll(); // Llama al método findAll del repositorio
    }

    //Obtener un pedido por ID
    public Optional<PedidoModel> ObtenerPedidoPorId(Long id) { // Método para obtener un pedido por su ID
        return pedidoRepository.findById(id); // Llama al método findById del repositorio
    }

    //Guardar un nuevo pedido
    public PedidoModel guardarPedido(PedidoModel pedidoModel) { // Método para guardar un nuevo pedido
        return pedidoRepository.save(pedidoModel); // Llama al método save del repositorio para guardar el pedido
    }

    // Obtener por dirección entrega.
    public List<PedidoModel> obtenerPedidoPorDireccionEntrega(String direccionEntrega){
        return pedidoRepository.findByDireccionEntrega(direccionEntrega);
    }

    // Obtener por estado.
    public List<PedidoModel> obtenerPedidoPorEstado(String estadoPedido){
        return pedidoRepository.findByEstado(estadoPedido);
    }


    // Método actualizarPedido.
    public PedidoModel actualizarPedido(Long idPedido, PedidoModel pedidoModel){
        // Crea un objeto con atribnutos y métodos de Pedido.
        PedidoModel pedidoExistente = pedidoRepository.findById(idPedido).orElse(null);
    // Si el objeto es diferente a nada. Es decir: Existe xd.
    if(pedidoExistente != null){
        // Modifica los cambios de dirección y estado.
        pedidoExistente.setDireccionEntrega(pedidoModel.getDireccionEntrega());
        pedidoExistente.setEstado(pedidoModel.getEstado());
        // Guarda.
        return pedidoRepository.save(pedidoExistente);
    } else{
        return null;
    }
}

    // Actualizar un pedido parcial.
    public PedidoModel ActualizarPedidoParcial(Long id, PedidoModel pedidoDetalles) {
        PedidoModel pedidoExistente = pedidoRepository.findById(id) // Busca el pedido por ID
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id)); // Lanza una excepción si no se encuentra el pedido

        if (pedidoDetalles.getDireccionEntrega() != null) { // Verifica si la dirección de entrega no es nula
            pedidoExistente.setDireccionEntrega(pedidoDetalles.getDireccionEntrega()); // Actualiza la dirección de entrega
        }

        if (pedidoDetalles.getEstado() != null) { // Verifica si el estado no es nulo
            pedidoExistente.setEstado(pedidoDetalles.getEstado()); // Actualiza el estado del pedido
        }
        return pedidoRepository.save(pedidoExistente); // Guarda los cambios en el pedido existente
    }

    //Eliminar un pedido por ID
    public void eliminarPedido(Long id) {
        // 1. Busca el pedido.
        PedidoModel pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con id: " + id));

        // 2. Busca y elimina sus detalles.
        List<DetallePedidoModel> detalles = detallePedidoRepository.findByPedido(pedido);
        detallePedidoRepository.deleteAll(detalles);

        // 3. Elimina el pedido principal.
        pedidoRepository.delete(pedido);
    }

    

    // Método que obtiene el PedidoResumen con los datos.
    public List<Map<String, Object>> obtenerPedidosConDatos(){
        // Existe en repository
        // Creamos una lista que guarde objetos con datos.
        List<Object[]> resultados = pedidoRepository.findByPedidosResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        // Recorre la lista.
        for(Object[] fila : resultados){
            // HashMap: Similar a Map solo que no garantiza ningún orden.
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID: ", fila[0]);
            datos.put("Dirección Entrega: ", fila[1]);
            datos.put("Estado: ", fila[2]);
            datos.put("Usuario: ", fila[3]);

            // Agrega a la lista.
            lista.add(datos);
        }
        return lista;
    }

}