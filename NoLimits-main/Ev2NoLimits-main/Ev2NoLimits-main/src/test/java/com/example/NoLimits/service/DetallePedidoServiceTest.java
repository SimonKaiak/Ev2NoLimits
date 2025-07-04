package com.example.NoLimits.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.DetallePedidoModel;
import com.example.NoLimits.Multimedia.model.PedidoModel;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
import com.example.NoLimits.Multimedia.service.DetallePedidoService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class DetallePedidoServiceTest {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

    @Test
    public void testFindAll() {
        // Busca todos los detalles de pedido.
        when(detallePedidoRepository.findAll()).thenReturn(List.of(detallePedido())); // Retorna una lista de detalles
                                                                                      // de pedido.
        List<DetallePedidoModel> detalles = detallePedidoService.findAll(); // Busca todos los detalles de pedido.
        assertNotNull(detalles); // Verifica que la lista de detalles no sea nula.
        assertEquals(1, detalles.size()); // Verifica que la lista de detalles tenga un elemento.
    }

    @Test
    public void testFindById() {
        // Busca un detalle de pedido por su ID.
        when(detallePedidoRepository.findById(1L)).thenReturn(java.util.Optional.of(detallePedido()));
        DetallePedidoModel detallePedido = detallePedidoService.findById(1L).orElse(null); // Busca un detalle de pedido
                                                                                           // por su ID y retorna un
                                                                                           // Optional.
        assertNotNull(detallePedido); // Verifica que el detalle de pedido no sea nula.
        assertEquals(1L, detallePedido.getIdDetallePedido()); // Verifica que el ID del detalle de pedido sea correcto.
        assertEquals(2, detallePedido.getCantidad());
        assertEquals(10.0f, detallePedido.getSubtotal()); // Verifica que el subtotal del detalle de pedido sea correcto.
        assertEquals(5.0f, detallePedido.getPrecioUnitario()); // Verifica que el precio unitario del detalle de pedido
                                                               // sea correcto.
    }

    @Test
    public void testSave() {
        // Guarda un detalle de pedido.
        when(detallePedidoRepository.save(any(DetallePedidoModel.class))).thenReturn(detallePedido()); // Retorna el
                                                                                                       // detalle de
                                                                                                       // pedido
                                                                                                       // guardado.
        DetallePedidoModel detallePedido = detallePedido();
        DetallePedidoModel savedDetallePedido = detallePedidoService.save(detallePedido); // Guarda el detalle de
                                                                                          // pedido.
        assertNotNull(savedDetallePedido); // Verifica que el detalle de pedido guardado no sea nulo.
        assertEquals(1L, savedDetallePedido.getIdDetallePedido()); // Verifica que el ID del detalle de pedido guardado
                                                                   // sea correcto.
        assertEquals(2, savedDetallePedido.getCantidad()); // Verifica que la cantidad del detalle de pedido guardado
                                                           // sea correcta.
        assertEquals(10.0f, savedDetallePedido.getSubtotal()); // Verifica que el subtotal del detalle de pedido guardado
                                                              // sea correcto.
        assertEquals(5.0f, savedDetallePedido.getPrecioUnitario()); // Verifica que el precio unitario del detalle de
                                                                    // pedido guardado sea correcto.
    }

        @Test
    public void testPatchDetallePedido() {
        // Crea el detalle de pedido original con datos base
        DetallePedidoModel detallePedido = detallePedido(); // Este debe tener ID = 1L
        detallePedido.setIdDetallePedido(1L); // Por si el método detallePedido() no lo tiene asignado

        // Crea los datos que se van a usar para el patch
        DetallePedidoModel patchData = new DetallePedidoModel();
        patchData.setCantidad(3);              // Cambia la cantidad a 3
        patchData.setSubtotal(15.0f);          // Cambia el subtotal a 15.0
        patchData.setPrecioUnitario(5.0f);     // Cambia el precio unitario a 5.0f
        patchData.setIdDetallePedido(1L);      // Le asignamos el mismo ID

        // Simulaciones del repositorio
        when(detallePedidoRepository.findById(1L)).thenReturn(Optional.of(detallePedido));
        when(detallePedidoRepository.save(any(DetallePedidoModel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0)); // Retorna el objeto actualizado

        // Ejecuta el método PATCH del servicio
        DetallePedidoModel patchedDetallePedido = detallePedidoService.patch(1L, patchData);

        // Verificaciones
        assertNotNull(patchedDetallePedido);
        assertEquals(1L, patchedDetallePedido.getIdDetallePedido());
        assertEquals(3, patchedDetallePedido.getCantidad());
        assertEquals(15.0f, patchedDetallePedido.getSubtotal());
        assertEquals(5.0f, patchedDetallePedido.getPrecioUnitario());
    }

    @Test
    public void testDeleteById() {
        // Elimina un detalle de pedido por su ID.
        DetallePedidoModel detallePedido = detallePedido();
        when(detallePedidoRepository.findById(1L)).thenReturn(java.util.Optional.of(detallePedido));
        doNothing().when(detallePedidoRepository).deleteById(1L); // Configura el mock para no hacer nada al eliminar.
        detallePedidoService.deleteById(1L); // Elimina el detalle de pedido por su ID.
        verify(detallePedidoRepository, times(1)).deleteById(1L); // Verifica que el método deleteById fue llamado una
                                                                  // vez.
        // Verifica que el detalle de pedido ya no existe.
        when(detallePedidoRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        DetallePedidoModel deletedDetallePedido = detallePedidoService.findById(1L).orElse(null);
        assertEquals(null, deletedDetallePedido); // Verifica que el detalle de pedido eliminado sea nulo.
    }
    
    private DetallePedidoModel detallePedido() {
        DetallePedidoModel detalle = new DetallePedidoModel();
        detalle.setIdDetallePedido(1L);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(5.0f);
        detalle.setSubtotal(10.0f);

        PedidoModel pedido = new PedidoModel();
        pedido.setId(1L);
        detalle.setPedido(pedido);

        AccesorioModel accesorio = new AccesorioModel();
        accesorio.setIdAccesorio(1L);
        detalle.setAccesorio(accesorio);

        return detalle;
    }
}
