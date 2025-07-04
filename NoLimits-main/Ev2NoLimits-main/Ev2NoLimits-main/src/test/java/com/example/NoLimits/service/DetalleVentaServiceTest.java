package com.example.NoLimits.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.service.DetalleVentaService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class DetalleVentaServiceTest {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @MockBean
    private DetalleVentaRepository detalleVentaRepository;

    private DetalleVentaModel detalleVenta() {
        DetalleVentaModel detalleVenta = new DetalleVentaModel();
        detalleVenta.setIdDetalleVenta(1L);
        detalleVenta.setCantidad(2);
        detalleVenta.setSubtotal(10.0f);
        detalleVenta.setPrecioUnitario(5.0f);
        detalleVenta.setVenta(null); // Asignar una venta válida si es necesario
        detalleVenta.setAccesorio(null); // Asignar un accesorio válido si es necesario
        detalleVenta.setPelicula(null); // Asignar una película válida si es necesario
        detalleVenta.setVideojuego(null); // Asignar un videojuego válido si es necesario
        return detalleVenta;
    }

    @Test
    public void testFindAll() {
        // Busca todos los detalles de venta.
        when(detalleVentaRepository.findAll()).thenReturn(List.of(detalleVenta()));
        List<DetalleVentaModel> detalles = detalleVentaService.findAll(); // Busca todos los detalles de venta.
        assertNotNull(detalles); // Verifica que la lista de detalles no sea nula
        assertEquals(1, detalles.size()); // Verifica que la lista de detalles tenga un elemento
    }

    @Test
    public void testFindById() {
        // Busca un detalle de venta por su id.
        when(detalleVentaRepository.findById(1L)).thenReturn(java.util.Optional.of(detalleVenta()));
        DetalleVentaModel detalleVenta = detalleVentaService.findById(1L).orElse(null); // Busca un detalle de venta por
                                                                                        // su id y retorna un Optional.
        assertNotNull(detalleVenta); // Verifica que el detalle de venta no sea nulo
        assertEquals(1L, detalleVenta.getIdDetalleVenta()); // Verifica que el id del detalle de venta sea correcto
        assertEquals(2, detalleVenta.getCantidad()); // Verifica que la cantidad del detalle de venta sea correcta
        assertEquals(10.0f, detalleVenta.getSubtotal()); // Verifica que el subtotal del detalle de venta sea correcto
        assertEquals(5.0f, detalleVenta.getPrecioUnitario()); // Verifica que el precio unitario del detalle de venta
                                                              // sea correcto
    }

    @Test
    public void testSave() {
        // Guarda un nuevo detalle de venta.
        when(detalleVentaRepository.save(any(DetalleVentaModel.class))).thenReturn(detalleVenta());
        DetalleVentaModel detalleVenta = detalleVentaService.save(detalleVenta()); // Guarda un nuevo detalle de venta
        assertNotNull(detalleVenta); // Verifica que el detalle de venta no sea nulo
        assertEquals(1L, detalleVenta.getIdDetalleVenta());
        assertEquals(2, detalleVenta.getCantidad()); // Verifica que la cantidad del detalle de venta sea correcta
        assertEquals(10.0f, detalleVenta.getSubtotal()); // Verifica que el subtotal del detalle de venta sea correcto
        assertEquals(5.0f, detalleVenta.getPrecioUnitario()); // Verifica que el precio unitario del detalle de venta
                                                              // sea
    }

    @Test
    public void testPatchDetalleVenta() {
        // Crea un detalle de venta existente simulado (mock).
        DetalleVentaModel existingDetalleVenta = detalleVenta();

        // Crea un objeto con los nuevos datos que se quieren actualizar.
        DetalleVentaModel patchData = new DetalleVentaModel();
        patchData.setIdDetalleVenta(1L); // Se asigna el ID esperado para validación
        patchData.setCantidad(3); // Nueva cantidad
        patchData.setSubtotal(15.0f); // Nuevo subtotal
        patchData.setPrecioUnitario(5.0f); // Nuevo precio unitario

        // Configura el comportamiento simulado del repositorio:
        // Cuando se busca el ID 1, devuelve el detalle existente
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existingDetalleVenta));

        // Cuando se guarda cualquier objeto DetalleVentaModel, retorna patchData (simulación de guardado)
        when(detalleVentaRepository.save(any(DetalleVentaModel.class))).thenReturn(patchData);

        // Ejecuta el método que se quiere probar
        DetalleVentaModel patchedDetalleVenta = detalleVentaService.patchDetalleVentaModel(1L, patchData);

        // Verificaciones: asegura que los cambios se hayan aplicado correctamente
        assertNotNull(patchedDetalleVenta); // Verifica que el resultado no sea null
        assertEquals(1L, patchedDetalleVenta.getIdDetalleVenta()); // Verifica que el ID sea el esperado
        assertEquals(3, patchedDetalleVenta.getCantidad()); // Verifica que la cantidad sea 3
        assertEquals(15.0f, patchedDetalleVenta.getSubtotal()); // Verifica el subtotal
        assertEquals(5.0f, patchedDetalleVenta.getPrecioUnitario()); // Verifica el precio unitario
    }

    @Test
    public void testDelete() {
        // Elimina un detalle de venta por su id.
        doNothing().when(detalleVentaRepository).deleteById(1L); // Configura el mock para que no haga nada al eliminar
                                                                 // el detalle de venta
        detalleVentaService.deleteById(1L); // Elimina el detalle de venta por su id
        verify(detalleVentaRepository, times(1)).deleteById(1L); // Verifica que se haya llamado al método deleteById
                                                                 // del repositorio una vez
    }
}
