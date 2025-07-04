package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.example.NoLimits.Multimedia.model.VideojuegoModel;
import com.example.NoLimits.Multimedia.repository.VideojuegoRepository;
import com.example.NoLimits.Multimedia.service.VideojuegoService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class VideojuegoServiceTest {

    @Autowired
    private VideojuegoService videojuegoService;

    @MockBean
    private VideojuegoRepository videojuegoRepository;
//---------------------------------------------------------------------------------------------------------------------------------------------------
    // Método auxiliar que crea un VideojuegoModel con ID 1
    private VideojuegoModel createVideojuego() {
        VideojuegoModel videojuego = new VideojuegoModel();
        videojuego.setIdVideojuego(1L);
        videojuego.setNombreVideojuego("The Legend of Zelda: Breath of the Wild");
        videojuego.setCategoriaVideojuego("Aventura");
        videojuego.setPlataformaVideojuego("Nintendo Switch");
        videojuego.setDesarrolladorVideojuego("Nintendo");
        videojuego.setDescripcionVideojuego("Un juego de aventura en un mundo abierto.");
        videojuego.setPrecioVideojuego(59.99f);
        return videojuego;
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca todos los videojuegos.
    // Este método busca todos los videojuegos y retorna una lista.
    public void testFindAll() {
        when(videojuegoRepository.findAll()).thenReturn(List.of(createVideojuego())); // Retorna una lista de videojuegos.
        List<VideojuegoModel> videojuegos = videojuegoService.obtenerVideojuegos(); // Busca todos los videojuegos.
        assertNotNull(videojuegos); // Verifica que la lista de videojuegos no sea nula.
        assertEquals(1, videojuegos.size()); // Verifica que la lista de videojuegos tenga un elemento.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca un videojuego por ID.
    // Este método busca un videojuego por su ID y retorna el videojuego encontrado.
    public void testFindById() {
        when(videojuegoRepository.findById(1L)).thenReturn(Optional.of(createVideojuego())); // Retorna un videojuego con ID 1.
        VideojuegoModel videojuego = videojuegoService.obtenerVideojuegoPorId(1L); // Busca el videojuego por ID.
        assertNotNull(videojuego); // Verifica que el videojuego no sea nulo.
        assertEquals("The Legend of Zelda: Breath of the Wild", videojuego.getNombreVideojuego()); // Verifica que el nombre del videojuego sea "The Legend of Zelda: Breath of the Wild".
        assertEquals("Nintendo", videojuego.getDesarrolladorVideojuego()); // Verifica que el desarrollador del videojuego sea "Nintendo".
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Guarda un nuevo videojuego.
    // Este método guarda un nuevo videojuego y retorna el videojuego guardado.
    public void testSave() {
        VideojuegoModel videojuego = createVideojuego(); // Crea un videojuego de ejemplo.
        when(videojuegoRepository.save(any(VideojuegoModel.class))).thenReturn(videojuego); // Simula el guardado del videojuego en el repositorio.
        VideojuegoModel savedVideojuego = videojuegoService.guardarVideojuego(videojuego); // Guarda el videojuego.
        assertNotNull(savedVideojuego); // Verifica que el videojuego guardado no sea nulo.
        assertEquals("The Legend of Zelda: Breath of the Wild", savedVideojuego.getNombreVideojuego()); // Verifica que el nombre del videojuego guardado sea "The Legend of Zelda: Breath of the Wild".
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza un videojuego existente.
    // Este método actualiza un videojuego existente y retorna el videojuego actualizado.
    public void testUpdate() {
        VideojuegoModel existingVideojuego = createVideojuego(); // Crea un videojuego existente.
        VideojuegoModel newDetails = new VideojuegoModel(); // Crea un nuevo videojuego con los detalles actualizados.
        newDetails.setNombreVideojuego("Super Mario Odyssey"); // Cambia el nombre del videojuego.
        newDetails.setPrecioVideojuego(49.99f); // Cambia el precio del videojuego.

        when(videojuegoRepository.findById(1L)).thenReturn(Optional.of(existingVideojuego)); // Simula la búsqueda del videojuego existente por ID.
        when(videojuegoRepository.save(any(VideojuegoModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simula el guardado del videojuego actualizado en el repositorio. 

        VideojuegoModel updatedVideojuego = videojuegoService.actualizarVideojuego(1L, newDetails); // Actualiza el videojuego con los nuevos detalles.

        assertNotNull(updatedVideojuego); // Verifica que el videojuego actualizado no sea nulo.
        assertEquals("Super Mario Odyssey", updatedVideojuego.getNombreVideojuego()); // Verifica que el nombre del videojuego actualizado sea "Super Mario Odyssey".
        assertEquals(49.99f, updatedVideojuego.getPrecioVideojuego()); // Verifica que el nombre y el precio del videojuego actualizado sean correctos.
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza parcialmente un videojuego existente.
    // Este método actualiza parcialmente un videojuego existente y retorna el videojuego actualizado.
    public void testPatch() {
        VideojuegoModel existingVideojuego = createVideojuego(); // Crea un videojuego existente.
        VideojuegoModel patchData = new VideojuegoModel(); // Crea un nuevo videojuego con los datos a actualizar.
        patchData.setPrecioVideojuego(69.99f); // Cambia el precio del videojuego.

        when(videojuegoRepository.findById(1L)).thenReturn(Optional.of(existingVideojuego)); // Simula la búsqueda del videojuego existente por ID.
        when(videojuegoRepository.save(any(VideojuegoModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simula el guardado del videojuego actualizado en el repositorio.
        
        VideojuegoModel patchedVideojuego = videojuegoService.actualizarVideojuegoParcial(1L, patchData); // Actualiza parcialmente el videojuego con los nuevos datos.

        assertNotNull(patchedVideojuego); // Verifica que el videojuego actualizado no sea nulo.
        assertEquals(69.99f, patchedVideojuego.getPrecioVideojuego()); // Verifica que el precio del videojuego actualizado sea 69.99.
        assertEquals("The Legend of Zelda: Breath of the Wild", patchedVideojuego.getNombreVideojuego()); // Verifica que el nombre del videojuego actualizado siga siendo "The Legend of Zelda: Breath of the Wild". 
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Elimina un videojuego por su ID existente.
    // Este método verifica que se llame correctamente al repositorio para eliminar un videojuego.
    public void testDeleteById() {
        // Crea un videojuego simulado con ID 1L
        VideojuegoModel videojuego = createVideojuego();

        // Simula que el videojuego existe en el repositorio
        when(videojuegoRepository.findById(1L)).thenReturn(Optional.of(videojuego));

        // Simula que la eliminación no hace nada (es un método void)
        doNothing().when(videojuegoRepository).delete(videojuego);

        // Llama al método del servicio para eliminar el videojuego
        videojuegoService.eliminarVideojuegoPorId(1L);

        // Verifica que se haya llamado al método delete con el objeto correcto
        verify(videojuegoRepository, times(1)).delete(videojuego);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Intenta eliminar un videojuego que no existe.
    // Verifica que se lance la excepción RecursoNoEncontradoException correctamente.
    public void testDeleteById_NoExiste() {
        // Simula que no existe un videojuego con ID 99L
        when(videojuegoRepository.findById(99L)).thenReturn(Optional.empty());

        // Verifica que se lanza la excepción personalizada del sistema
        assertThrows(RecursoNoEncontradoException.class, () -> {
            videojuegoService.eliminarVideojuegoPorId(99L);
        });

        // Verifica que NO se haya llamado al método delete
        verify(videojuegoRepository, times(0)).delete(any(VideojuegoModel.class));
    }
}
