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

import com.example.NoLimits.Multimedia.model.PedidoModel;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
import com.example.NoLimits.Multimedia.repository.PedidoRepository;
import com.example.NoLimits.Multimedia.service.PedidoService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

//---------------------------------------------------------------------------------------------------------------------------------------------------
    // Crea un pedido de ejemplo.
    // Este método crea un objeto PedidoModel con un ID, una dirección de entrega y un estado.
    private PedidoModel createPedido() {
        PedidoModel pedido = new PedidoModel();
        pedido.setId(1L);
        pedido.setDireccionEntrega("Calle Parker 123");
        pedido.setEstado("En Proceso");
        return pedido;
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca todos los pedidos.
    // Este método busca todos los pedidos y retorna una lista.
    public void testFindAll() {
        when(pedidoRepository.findAll()).thenReturn(List.of(createPedido())); // Retorna una lista de pedidos.
        List<PedidoModel> pedidos = pedidoService.ObtenerTodosLosPedidos(); // Busca todos los pedidos.
        assertNotNull(pedidos); // Verifica que la lista de pedidos no sea nula.
        assertEquals(1, pedidos.size()); // Verifica que la lista de pedidos tenga un elemento.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca un pedido por su ID.
    // Este método busca un pedido por su ID y retorna un Optional.
    public void testFindById() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(createPedido())); // Busca un pedido por su ID.
        Optional<PedidoModel> pedidoOptional = pedidoService.ObtenerPedidoPorId(1L); // Busca un pedido por su ID y retorna un Optional.
        PedidoModel pedido = pedidoOptional.orElse(null); // Obtiene el pedido del Optional. 
        assertNotNull(pedido); // Verifica que el pedido no sea nulo.
        assertEquals("En Proceso", pedido.getEstado()); // Verifica que el estado del pedido sea correcto.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Guarda un nuevo pedido.
    // Este método guarda un nuevo pedido y retorna el pedido guardado.
    public void testSave() {
        PedidoModel pedido = createPedido(); // Crea un nuevo pedido.
        when(pedidoRepository.save(pedido)).thenReturn(pedido); // Guarda el pedido en el repositorio.
        PedidoModel savedPedido = pedidoService.guardarPedido(pedido); // Guarda el pedido en el servicio.
        assertNotNull(savedPedido); // Verifica que el pedido guardado no sea nulo.
        assertEquals("En Proceso", savedPedido.getEstado()); // Verifica que el estado del pedido guardado sea correcto.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza un pedido existente.
    // Este método actualiza un pedido existente y retorna el pedido actualizado.
    public void testUpdate() {
        PedidoModel existingPedido = createPedido(); // Crea un pedido existente.
        PedidoModel newDetails = new PedidoModel(); // Crea un nuevo pedido con los detalles a actualizar.
        newDetails.setEstado("Enviado"); // Establece el nuevo estado del pedido.
        newDetails.setDireccionEntrega("Avenida Siempreviva 742"); // Establece una nueva dirección de entrega.

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(existingPedido)); // Busca un pedido por su ID.
        when(pedidoRepository.save(any(PedidoModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Guarda el pedido actualizado en el repositorio.

        PedidoModel updatedPedido = pedidoService.actualizarPedido(1L, newDetails); // Actualiza el pedido y obtiene el pedido actualizado.
        assertNotNull(updatedPedido); // Verifica que el pedido actualizado no sea nulo.
        assertEquals("Enviado", updatedPedido.getEstado()); // Verifica que el estado del pedido actualizado sea correcto.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza parcialmente un pedido existente.
    // Este método actualiza parcialmente un pedido existente y retorna el pedido actualizado.
    public void testPatch() {
        PedidoModel existingPedido = createPedido(); // Crea un pedido existente.
        PedidoModel patchDetails = new PedidoModel(); // Crea un nuevo pedido con los detalles a actualizar.
        patchDetails.setEstado("Entregado"); // Establece el nuevo estado del pedido.

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(existingPedido)); // Busca un pedido por su ID.
        when(pedidoRepository.save(any(PedidoModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Guarda el pedido actualizado en el repositorio.

        PedidoModel patchedPedido = pedidoService.ActualizarPedidoParcial(1L, patchDetails); // Actualiza parcialmente el pedido y obtiene el pedido actualizado.
        assertNotNull(patchedPedido); // Verifica que el pedido actualizado no sea nulo.
        assertEquals("Entregado", patchedPedido.getEstado()); // Verifica que el estado del pedido actualizado sea correcto.
        assertEquals("Calle Parker 123", patchedPedido.getDireccionEntrega()); // Verifica que la dirección de entrega no haya cambiado.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
public void testDeleteById() {
    PedidoModel pedido = new PedidoModel();
    pedido.setId(1L);

    when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
    when(detallePedidoRepository.findByPedido(pedido)).thenReturn(List.of());

    doNothing().when(detallePedidoRepository).deleteAll(any());
    doNothing().when(pedidoRepository).delete(any(PedidoModel.class));

    pedidoService.eliminarPedido(1L);

    verify(detallePedidoRepository, times(1)).deleteAll(any());
    verify(pedidoRepository, times(1)).delete(any(PedidoModel.class));
}
//---------------------------------------------------------------------------------------------------------------------------------------------------
}
