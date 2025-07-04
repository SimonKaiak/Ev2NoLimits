package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.repository.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.service.MetodoPagoService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class MetodoPagoServiceTest {

    @Autowired
    private MetodoPagoService metodoPagoService;
    
    @MockBean
    private MetodoPagoRepository metodoPagoRepository;

//---------------------------------------------------------------------------------------------------------------------------------------------------
    // Crea un metodo de pago de ejemplo.
    // Este método crea un objeto MetodoPagoModel con un ID y un nombre.
    private MetodoPagoModel createMetodoPago() {
        MetodoPagoModel metodoPago = new MetodoPagoModel();
        metodoPago.setId(1L);
        metodoPago.setNombre("Tarjeta de Crédito");
        return metodoPago;
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca todos los metodos de pago.
    // Este método busca todos los metodos de pago y retorna una lista.
    public void testFindAll() {
        when(metodoPagoRepository.findAll()).thenReturn(List.of(createMetodoPago())); // Retorna una lista de metodos de pago.
        List<MetodoPagoModel> metodos = metodoPagoService.findAll(); // Busca todos los metodos de pago.
        assertNotNull(metodos); // Verifica que la lista de metodos de pago no sea nula.
        assertEquals(1, metodos.size()); // Verifica que la lista de metodos de pago tenga un elemento.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca un metodo de pago por su ID.
    // Este método busca un metodo de pago por su ID y retorna un Optional.
    public void testFindById() { 
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(createMetodoPago())); // Busca un metodo de pago por su ID.
        Optional<MetodoPagoModel> metodoPagoOptional = metodoPagoService.findById(1L); // Busca un metodo de pago por su ID y retorna un Optional.
        MetodoPagoModel metodoPago = metodoPagoOptional.orElse(null);
        assertNotNull(metodoPago); // Verifica que el metodo de pago no sea nulo.
        assertEquals("Tarjeta de Crédito", metodoPago.getNombre()); // Verifica que el nombre del metodo de pago sea correcto.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Guarda un nuevo metodo de pago.
    // Este método crea un nuevo metodo de pago y lo guarda en el repositorio.
    public void testSave() {
        MetodoPagoModel metodoPago = createMetodoPago(); // Crea un nuevo metodo de pago.
        when(metodoPagoRepository.save(metodoPago)).thenReturn(metodoPago); // Guarda el metodo de pago en el repositorio.
        MetodoPagoModel savedMetodoPago = metodoPagoService.save(metodoPago); // Guarda el metodo de pago en el servicio.
        assertNotNull(savedMetodoPago); // Verifica que el metodo de pago guardado no sea nulo.
        assertEquals("Tarjeta de Crédito", savedMetodoPago.getNombre()); // Verifica que el nombre del metodo de pago guardado sea correcto.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza un metodo de pago existente.
    // Este método actualiza un metodo de pago existente con nuevos detalles.
    public void testUpdate() { 
        MetodoPagoModel existingMetodoPago = createMetodoPago(); // Crea un metodo de pago existente.
        MetodoPagoModel newDetails = new MetodoPagoModel(); // Crea un nuevo metodo de pago con los detalles a actualizar.
        newDetails.setNombre("PayPal"); // Establece el nuevo nombre del metodo de pago.

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existingMetodoPago)); // Busca el metodo de pago existente por su ID.
        when(metodoPagoRepository.save(any(MetodoPagoModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Guarda el metodo de pago actualizado en el repositorio.

        MetodoPagoModel updatedMetodoPago = metodoPagoService.update(1L, newDetails); // Actualiza el metodo de pago con los nuevos detalles.
        assertNotNull(updatedMetodoPago); // Verifica que el metodo de pago actualizado no sea nulo.
        assertEquals("PayPal", updatedMetodoPago.getNombre()); // Verifica que el nombre del metodo de pago actualizado sea correcto.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Aplica un parche a un metodo de pago existente.
    // Este método aplica un parche a un metodo de pago existente, actualizando solo los campos necesarios.
    public void testPatch() {
        MetodoPagoModel existingMetodoPago = createMetodoPago(); // Crea un metodo de pago existente.
        MetodoPagoModel patchDetails = new MetodoPagoModel(); // Crea un nuevo metodo de pago con los detalles del parche.
        patchDetails.setNombre("Transferencia Bancaria"); // Establece el nuevo nombre del metodo de pago.

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existingMetodoPago)); // Busca el metodo de pago existente por su ID.
        when(metodoPagoRepository.save(any(MetodoPagoModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Guarda el metodo de pago actualizado en el repositorio.

        MetodoPagoModel patchedMetodoPago = metodoPagoService.patch(1L, patchDetails); // Aplica el parche al metodo de pago existente.
        assertNotNull(patchedMetodoPago); // Verifica que el metodo de pago parcheado no sea nulo.
        assertEquals("Transferencia Bancaria", patchedMetodoPago.getNombre()); // Verifica que el nombre del metodo de pago parcheado sea correcto.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testDeleteById() {
        // Simula que sí existe un método de pago con ID 1
        MetodoPagoModel metodo = new MetodoPagoModel();
        metodo.setId(1L);
        metodo.setNombre("Débito");

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodo));
        doNothing().when(metodoPagoRepository).delete(metodo);

        // Ejecuta el método a probar
        metodoPagoService.deleteById(1L);

        // Verifica que se haya llamado correctamente
        verify(metodoPagoRepository, times(1)).delete(metodo);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
}
