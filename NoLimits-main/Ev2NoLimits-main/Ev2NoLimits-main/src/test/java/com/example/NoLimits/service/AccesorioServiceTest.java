package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.AccesorioModel;
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.model.VideojuegoModel;
import com.example.NoLimits.Multimedia.repository.AccesorioRepository;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.PeliculaRepository;
import com.example.NoLimits.Multimedia.repository.VideojuegoRepository;
import com.example.NoLimits.Multimedia.service.AccesorioService;

import jakarta.transaction.Transactional;

// Carga todo el contexto de la app pero solo para pruebas.
@SpringBootTest
// Cada mpetodo test se ejecuta dentro de una transacción.
@Transactional
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class AccesorioServiceTest {

    @Autowired
    private AccesorioService accesorioService;

    @MockBean
    private AccesorioRepository accesorioRepository;

    @MockBean
    private PeliculaRepository peliculaRepository;

    @MockBean
    private VideojuegoRepository videojuegoRepository;

    @MockBean
    private DetalleVentaRepository detalleVentaRepository;
    
    @MockBean
    private DetallePedidoRepository detallePedidoRepository;


//---------------------------------------------------------------------------------------------------------------------------------------------------
    // Crea un accesorio de ejemplo.
    // Este método crea un objeto AccesorioModel con un ID, nombre, marca,
    private AccesorioModel createAccesorio() {
        PeliculaModel pelicula = new PeliculaModel();
        pelicula.setIdPelicula(1L);
        VideojuegoModel videojuego = new VideojuegoModel();
        videojuego.setIdVideojuego(1L);
        AccesorioModel accesorio = new AccesorioModel();
        accesorio.setIdAccesorio(1L);
        accesorio.setNombreAccesorio("Control Inalámbrico Spider-Man");
        accesorio.setMarcaAccesorio("Sony");
        accesorio.setTipoAccesorio("Consola");
        accesorio.setPrecioAccesorio(59990.0f);
        accesorio.setStockAccesorio(100);
        accesorio.setPelicula(pelicula);
        accesorio.setVideojuego(videojuego);
        return accesorio;
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca todos los accesorios.
    // Este método busca todos los accesorios y retorna una lista.
    public void testFindAll() {
        when(accesorioRepository.findAll()).thenReturn(List.of(createAccesorio())); // Retorna una lista de accesorios.
        
        List<AccesorioModel> accesorios = accesorioService.obtenerAccesorios(); // Busca todos los accesorios.
        
        assertNotNull(accesorios, "La lista de accesorios no debería ser nula"); // Verifica que la lista de accesorios no sea nula.
        assertEquals(1, accesorios.size(), "Se esperaba exactamente un accesorio en la lista"); // Verifica que la lista de accesorios tenga un elemento.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca un accesorio por ID.
    // Este método busca un accesorio por su ID y retorna el accesorio encontrado.
    public void testFindById() {
        when(accesorioRepository.findById(1L)).thenReturn(Optional.of(createAccesorio())); // Retorna un accesorio con ID 1.
    
        Optional<AccesorioModel> accesorio = accesorioService.obtenerAccesorioPorId(1L); // Busca el accesorio por ID.
        
        // Verifica que el accesorio fue encontrado (no está vacío).
        assertTrue(accesorio.isPresent(), "El accesorio con ID 1 debería estar presente");
        // Verifica que el nombre del accesorio coincida con lo esperado.
        assertEquals("Control Inalámbrico Spider-Man", accesorio.get().getNombreAccesorio(), "El nombre del accesorio no coincide");
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca un accesorio por su Id.
    // Este método busca un accesorio por su ID y retorna si el accesorio no existe.
    public void testFindByIdNoExiste() {
        when(accesorioRepository.findById(1L)).thenReturn(Optional.empty()); // Retorna un accesorio con ID 1.

        Optional<AccesorioModel> accesorio = accesorioService.obtenerAccesorioPorId(1L); // Busca el accesorio por ID.

        // Verifica que esté presente.
        assertFalse(accesorio.isPresent(), "No debería encontrarse un accesorio con ID 999");
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Guarda un nuevo accesorio.
    // Este método guarda un nuevo accesorio y retorna el accesorio guardado.
    public void testSave_Success() {
        AccesorioModel accesorio = createAccesorio(); // Crea un accesorio de ejemplo.
        
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(accesorio.getPelicula())); // Simula que la película existe.
        when(videojuegoRepository.findById(1L)).thenReturn(Optional.of(accesorio.getVideojuego())); // Simula que el videojuego existe.
        when(accesorioRepository.save(any(AccesorioModel.class))).thenReturn(accesorio); // Simula el guardado del accesorio en el repositorio.

        AccesorioModel savedAccesorio = accesorioService.guardarAccesorio(accesorio); // Guarda el accesorio.
        
        // Verifica que el accesorio guardado no sea nulo.
        assertNotNull(savedAccesorio, "El accesorio guardado no debería ser null"); 
        // Verifica que el nombre del accesorio guardado sea "Control Inalámbrico".
        assertEquals("Control Inalámbrico Spider-Man", savedAccesorio.getNombreAccesorio(),  "El nombre del accesorio guardado no coincide con el esperado"); 
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Intenta guardar un accesorio con una película que no existe.
    // Este método lanza una excepción si la película no se encuentra.
    public void testSave_MovieNotFound() {
        AccesorioModel accesorio = createAccesorio(); // Crea un accesorio de ejemplo.
        
        when(peliculaRepository.findById(1L)).thenReturn(Optional.empty()); // Simula que la pelicula no existe.

        // Exception: Captura la excepción.
        // assertThrows: Método que verifica que se lance una excepción.
        // RecursoNoEncontradoException: Espera que se lance la excepción específica.
        // () -> { accesorioService.guardarAccesorio(accesorio); Excepción lambda que ejecuta la excepción.. 
        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> { // Intenta guardar el accesorio.
            accesorioService.guardarAccesorio(accesorio); // Lanza una excepción si la película no se encuentra.
        }, "Se esperaba que se lanzara una excepción RecursoNoEncontradoException al no encontrar la película");
        assertEquals("Película no encontrada", exception.getMessage(), "El mensaje de la excepción no coincide con el esperado"); // Verifica que la excepción lanzada sea "Película no encontrada".
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Intenta guardar un accesorio con un videojuego que no existe.
    // Este método lanza una excepción si el videojuego no se encuentra.
    public void testUpdate() {
        AccesorioModel existingAccesorio = createAccesorio(); // Crea un accesorio existente.
        AccesorioModel newDetails = new AccesorioModel(); // Crea un nuevo accesorio con los detalles a actualizar.
        newDetails.setNombreAccesorio("Mouse Gamer"); // Cambia el nombre del accesorio.
        newDetails.setPrecioAccesorio(79990.0f); // Cambia el precio del accesorio.

        when(accesorioRepository.findById(1L)).thenReturn(Optional.of(existingAccesorio)); // Simula la busqueda del accesorio existente por ID.
        when(accesorioRepository.save(any(AccesorioModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simula el guardado del accesorio actualizado en el repositorio.
 
        // newDetails: Un objeto AccesorioModel con los nuevos valores (por ejemplo, nuevo nombre y precio).
        AccesorioModel updatedAccesorio = accesorioService.actualizarAccesorio(1L, newDetails); // Actualiza el accesorio con los nuevos detalles.
        
        assertNotNull(updatedAccesorio, "El accesorio actualizado no debería ser null"); // Verifica que el accesorio actualizado no sea nulo.
        assertEquals("Mouse Gamer", updatedAccesorio.getNombreAccesorio(), "El nombre del accesorio actualizado no coincide con el esperado"); // Verifica que el nombre del accesorio actualizado sea "Mouse Gamer".
        assertEquals(79990.0f, updatedAccesorio.getPrecioAccesorio(), "El precio del accesorio actualizado no coincide con el esperado"); // Verifica que el precio del accesorio actualizado sea 79990.0f.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza parcialmente un accesorio existente.
    // Este método actualiza parcialmente un accesorio existente y retorna el accesorio actualizado.
    public void testPatch() {
        AccesorioModel existingAccesorio = createAccesorio(); // Crea un accesorio existente.
        AccesorioModel patchDetails = new AccesorioModel(); // Crea un nuevo accesorio con los detalles a actualizar.
        patchDetails.setStockAccesorio(80); // Cambia el stock del accesorio.

        when(accesorioRepository.findById(1L)).thenReturn(Optional.of(existingAccesorio)); // Simula la búsqueda del accesorio existente por ID.
        when(accesorioRepository.save(any(AccesorioModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simula el guardado del accesorio actualizado en el repositorio.

        AccesorioModel patchedAccesorio = accesorioService.actualizarAccesorioParcial(1L, patchDetails); // Actualiza parcialmente el accesorio con los nuevos detalles.
        
        assertNotNull(patchedAccesorio, "El accesorio actualizado no debería ser null"); // Verifica que el accesorio actualizado no sea nulo.
        assertEquals(80, patchedAccesorio.getStockAccesorio(), "El stock del accesorio actualizado no coincide con el esperado"); // Verifica que el stock del accesorio actualizado sea 80.
        assertEquals("Control Inalámbrico Spider-Man", patchedAccesorio.getNombreAccesorio(), "El nombre del accesorio no debería haber cambiado en la actualización parcial"); // Verificar que el nombre no cambió
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    //Elimina un accesorio por su ID.
    // Este método simula que el accesorio existe, y que se eliminan correctamente los detalles y el accesorio.
     
    public void testDeleteById() {
        // Simula el accesorio existente
        AccesorioModel accesorio = createAccesorio(); // Método auxiliar que crea el accesorio

        // Simula que el accesorio existe en la base de datos
        when(accesorioRepository.findById(1L)).thenReturn(Optional.of(accesorio));

        // Simula la eliminación de detalles (voids)
        doNothing().when(detalleVentaRepository).deleteByAccesorio(accesorio);
        doNothing().when(detallePedidoRepository).deleteByAccesorio(accesorio);

        // Simula la eliminación del accesorio (void)
        doNothing().when(accesorioRepository).delete(accesorio);

        // Llama al método del servicio
        accesorioService.eliminarAccesorioPorId(1L);

        // Verifica que se eliminaron correctamente
        verify(detalleVentaRepository, times(1)).deleteByAccesorio(accesorio);
        verify(detallePedidoRepository, times(1)).deleteByAccesorio(accesorio);
        verify(accesorioRepository, times(1)).delete(accesorio);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Intenta eliminar un accesorio inexistente.
    // Verifica que se lance una excepción si no existe.
    public void testDeleteById_NoExiste() {
        // Simula que el accesorio con ID 99 no existe
        when(accesorioRepository.findById(99L)).thenReturn(Optional.empty());

        // Verifica que se lanza la excepción esperada
        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            accesorioService.eliminarAccesorioPorId(99L);
        });

        // Verifica el mensaje
        assertEquals("Accesorio no encontrado con ID: 99", exception.getMessage());

        // Verifica que NO se haya llamado a delete ni a los detalles
        verify(detalleVentaRepository, times(0)).deleteByAccesorio(any());
        verify(detallePedidoRepository, times(0)).deleteByAccesorio(any());
        verify(accesorioRepository, times(0)).delete(any());
    }
}
