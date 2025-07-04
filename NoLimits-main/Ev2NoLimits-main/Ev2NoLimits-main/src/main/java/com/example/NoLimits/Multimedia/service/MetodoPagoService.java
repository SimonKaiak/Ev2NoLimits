package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.repository.MetodoPagoRepository;
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
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    // Obtener todos los metodos de pago
    public List<MetodoPagoModel> findAll() { // Busca todos los metodos de pago.
        return metodoPagoRepository.findAll(); // Retorna una lista de metodos de pago.
    }

    // Obtener un metodo de pago por su ID
    public Optional<MetodoPagoModel> findById(Long id) { // Busca un metodo de pago por su ID.
        return metodoPagoRepository.findById(id); // Busca un metodo de pago por su ID y retorna un Optional.
    }

    // Guardar un nuevo metodo de pago
    public MetodoPagoModel save(MetodoPagoModel metodoPago) { // Guarda un nuevo metodo de pago.
        return metodoPagoRepository.save(metodoPago); // Guarda un nuevo metodo de pago y retorna el objeto guardado.   
    }

    // Eliminar un metodo de pago por su ID
    public void deleteById(Long id) {
        MetodoPagoModel metodo = metodoPagoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado con ID: " + id));

        metodoPagoRepository.delete(metodo);
    }

    // Obtener un metodo de pago por su nombre
    public Optional<MetodoPagoModel> findByNombre(String nombre) { // Busca un metodo de pago por su nombre.
        return metodoPagoRepository.findByNombre(nombre); // Busca un metodo de pago por su nombre y retorna un Optional.
    }

    // Actualizar un metodo de pago
    public MetodoPagoModel update(Long id, MetodoPagoModel metodoPagoDetails) { // Actualiza un metodo de pago.
        MetodoPagoModel metodoPago = metodoPagoRepository.findById(id) // Busca el metodo de pago por su ID.
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con id: " + id)); // Si no se encuentra, lanza una excepción.

        metodoPago.setNombre(metodoPagoDetails.getNombre()); // Actualiza el nombre del metodo de pago.
        return metodoPagoRepository.save(metodoPago); // Guarda el metodo de pago actualizado y lo retorna.
    }

    // Actualizar parcialmente un método de pago
    public MetodoPagoModel patch(Long id, MetodoPagoModel metodoPagoDetails) { // Actualiza parcialmente un método de pago.
        MetodoPagoModel metodoPago = metodoPagoRepository.findById(id) // Busca el método de pago por su ID.
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con id: " + id)); // Si no se encuentra, lanza una excepción.
        if (metodoPagoDetails.getNombre() != null) { // Si el nombre no es nulo, actualiza el nombre del método de pago.
            metodoPago.setNombre(metodoPagoDetails.getNombre()); 
        }
        return metodoPagoRepository.save(metodoPago); // Guarda el método de pago actualizado y lo retorna.
    }

    // Método que obtiene el MetodoPago con los datos.
    public List<Map<String, Object>> obtenerMetodoPagoConDatos(){
        // Existe en repository
        // Creamos una lista que guarde objetos con datos.
        List<Object[]> resultados = metodoPagoRepository.findMetodoPagoResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        // Recorre la lista.
        for(Object[] fila : resultados){
            // HashMap: Similar a Map solo que no garantiza ningún orden.
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID: ", fila[0]);
            datos.put("Nombre: ", fila[1]);
            datos.put("Ventas: ", fila[2]);

            // Agrega a la lista.
            lista.add(datos);
        }
        return lista;
    }

}
