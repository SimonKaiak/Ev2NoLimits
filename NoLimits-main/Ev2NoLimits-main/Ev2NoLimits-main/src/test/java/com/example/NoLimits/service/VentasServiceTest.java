package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.model.VentasModel;
import com.example.NoLimits.Multimedia.repository.VentasRepository;
import com.example.NoLimits.Multimedia.service.VentasService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class VentasServiceTest {

    @Autowired
    private VentasService ventasService;

    @MockBean
    private VentasRepository ventasRepository;
//---------------------------------------------------------------------------------------------------------------------------------------------------
    // Crea una venta de ejemplo.
    // Este método crea un objeto VentasModel con un ID, fecha de compra, hora
    private VentasModel ventas() {
        VentasModel ventas = new VentasModel();
        ventas.setId(1L);
        ventas.setFechaCompra(LocalDate.now());
        ventas.setHoraCompra(LocalTime.now());
        ventas.setTotalVenta(1000);
        ventas.setMetodoPagoModel(new MetodoPagoModel(1L, "Tarjeta de Crédito", null));
        ventas.setUsuarioModel(new UsuarioModel(1L, "Juan", "Pérez", "correo", 123456789, "password", null, null));
        return ventas;
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca todas las ventas.
    // Este método busca todas las ventas y retorna una lista.
    public void testFindAll() { // Busca todas las ventas.
        when(ventasRepository.findAll()).thenReturn(List.of(ventas())); // Retorna una lista de ventas.
        List<VentasModel> ventas = ventasService.findAll(); // Busca todas las ventas.
        assertNotNull(ventas); // Verifica que la lista de ventas no sea nula.
        assertEquals(1, ventas.size()); // Verifica que la lista de ventas tenga un
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca una venta por ID existente.
    // Verifica que se retorne la venta correcta cuando existe.
    public void testFindById() {
        // Simula una venta existente.
        VentasModel venta = ventas(); // Método auxiliar que crea una venta completa.

        // Simula que el repositorio retorna esa venta por ID.
        when(ventasRepository.findById(1L)).thenReturn(Optional.of(venta));

        // Llama al método del servicio (no usamos orElse porque el service ya maneja eso).
        VentasModel resultado = ventasService.findById(1L);

        // Verificaciones
        assertNotNull(resultado); // Asegura que la venta no sea nula.
        assertEquals(1000, resultado.getTotalVenta()); // Total de venta
        assertEquals("Tarjeta de Crédito", resultado.getMetodoPagoModel().getNombre()); // Método de pago
        assertEquals("Juan", resultado.getUsuarioModel().getNombre()); // Nombre usuario
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Guarda una venta.
    // Este método crea una nueva venta y la guarda en el repositorio.
    public void testSave() { // Guarda una venta.
        VentasModel ventas = ventas();
        when(ventasRepository.save(ventas)).thenReturn(ventas); // Retorna la venta
        VentasModel savedVentas = ventasService.save(ventas); // Guarda la venta.
        assertNotNull(savedVentas); // Verifica que la venta guardada no sea nula
        assertEquals(LocalDate.now(), savedVentas.getFechaCompra()); // Verifica que la fecha
        assertEquals(LocalTime.now().getHour(), savedVentas.getHoraCompra().getHour());
        assertEquals(1000, savedVentas.getTotalVenta()); // Verifica que el total
        assertEquals("Tarjeta de Crédito", savedVentas.getMetodoPagoModel().getNombre());
        assertEquals("Juan", savedVentas.getUsuarioModel().getNombre()); // Verifica que
        assertEquals("Pérez", savedVentas.getUsuarioModel().getApellidos()); // Verifica que los apellidos del usuario
                                                                             // sean correctos.
        assertEquals("correo", savedVentas.getUsuarioModel().getCorreo()); // Verifica que el correo del usuario sea
                                                                           // correcto.
        assertEquals(123456789, savedVentas.getUsuarioModel().getTelefono()); // Verifica que el telefono
        // del usuario sea correcto.
        assertEquals("password", savedVentas.getUsuarioModel().getPassword()); // Verifica que la contraseña del usuario
                                                                               // sea correcta.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza una venta existente.
    // Este método actualiza una venta existente con nuevos detalles.
    public void testPatchVentas() {
        VentasModel existingVentasModel = ventas();
        VentasModel patchData = new VentasModel();
        patchData.setTotalVenta(1500); // Cambiamos el total de la venta.

        when(ventasRepository.findById(1L)).thenReturn(java.util.Optional.of(existingVentasModel));
        when(ventasRepository.save(any(VentasModel.class))).thenReturn(existingVentasModel);

        VentasModel updatedVentas = ventasService.patchVentasModel(1L, patchData);

        assertNotNull(updatedVentas);
        assertEquals(1500, updatedVentas.getTotalVenta()); // Verifica que el total de la venta se haya actualizado.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Elimina una venta por ID existente.
    // Simula que la venta existe y verifica que se elimine correctamente.
    public void testDeleteById() {
        // Crea una venta simulada con ID 1L
        VentasModel venta = new VentasModel();
        venta.setId(1L);

        // Simula que la venta existe en el repositorio
        when(ventasRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventasRepository.existsById(1L)).thenReturn(true); // <-- ESTA ES LA CLAVE

        // Simula que al eliminar no ocurra nada (comportamiento normal del void)
        doNothing().when(ventasRepository).deleteById(1L);

        // Llama al método del servicio
        ventasService.deleteById(1L);

        // Verifica que el repositorio haya llamado al método deleteById
        verify(ventasRepository, times(1)).deleteById(1L);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Intenta eliminar una venta inexistente.
    // Verifica que se lance una excepción RecursoNoEncontradoException.
    public void testDeleteById_NoExiste() {
        when(ventasRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            ventasService.deleteById(99L);
        });

        verify(ventasRepository, times(0)).deleteById(anyLong());
    }
}
