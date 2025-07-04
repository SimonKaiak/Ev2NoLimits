package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.example.NoLimits.Multimedia.model.PeliculaModel;
import com.example.NoLimits.Multimedia.repository.AccesorioRepository;
import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.PeliculaRepository;
import com.example.NoLimits.Multimedia.service.PeliculaService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class PeliculaServiceTest {

    @Autowired
    private PeliculaService peliculaService;

    @MockBean
    private PeliculaRepository peliculaRepository;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

    @MockBean
    private DetalleVentaRepository detalleVentaRepository;

    @MockBean
    private AccesorioRepository accesorioRepository;

    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    // Crea una película de ejemplo.
    // Este método crea un objeto PeliculaModel con un ID, nombre, categoría y
    // duración
    private PeliculaModel createPelicula() {
        PeliculaModel pelicula = new PeliculaModel();
        pelicula.setIdPelicula(1L);
        pelicula.setNombrePelicula("SpiderMan");
        pelicula.setCategoriaPelicula("CienciaFiccion");
        pelicula.setDuracionPelicula(148);
        return pelicula;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca todas las películas.
    // Este método busca todas las películas y retorna una lista.
    public void testFindAll() {
        when(peliculaRepository.findAll()).thenReturn(List.of(createPelicula())); // Retorna una lista de películas.
        List<PeliculaModel> peliculas = peliculaService.obtenerPeliculas(); // Busca todas las películas.
        assertNotNull(peliculas); // Verifica que la lista de películas no sea nula.
        assertEquals(1, peliculas.size()); // Verifica que la lista de películas tenga un elemento.
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca una película por su ID.
    // Este método busca una película por su ID y retorna un Optional.
    public void testFindById() {
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(createPelicula())); // Busca una película por su
                                                                                         // ID.
        PeliculaModel pelicula = peliculaService.obtenerPeliculaPorId(1L); // Busca una película por su ID y retorna un
                                                                           // Optional.
        assertNotNull(pelicula); // Verifica que la película no sea nula.
        assertEquals("SpiderMan", pelicula.getNombrePelicula()); // Verifica que el nombre de la película sea correcto.
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Guarda una nueva película.
    // Este método guarda una nueva película y retorna el objeto guardado.
    public void testSave() {
        PeliculaModel pelicula = createPelicula(); // Crea una nueva película.
        when(peliculaRepository.save(pelicula)).thenReturn(pelicula); // Guarda la película en el repositorio.
        PeliculaModel savedPelicula = peliculaService.guardarPelicula(pelicula); // Guarda la película en el servicio.
        assertNotNull(savedPelicula); // Verifica que la película guardada no sea nula.
        assertEquals("SpiderMan", savedPelicula.getNombrePelicula()); // Verifica que el nombre de la película guardada
                                                                      // sea correcto.
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza una película existente.
    // Este método actualiza una película existente con nuevos detalles.
    public void testUpdate() {
        PeliculaModel existingPelicula = createPelicula(); // Crea una película existente con detalles iniciales.
        PeliculaModel newDetails = new PeliculaModel(); // Crea un nuevo objeto PeliculaModel para los detalles a
                                                        // actualizar.
        newDetails.setNombrePelicula("SpiderMan2"); // Establece el nuevo nombre de la película.
        newDetails.setCategoriaPelicula("CienciaFiccion"); // Establece la nueva categoría de la película.

        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(existingPelicula)); // Busca una película por su
                                                                                         // ID.
        when(peliculaRepository.save(any(PeliculaModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Guarda
                                                                                                                     // la
                                                                                                                     // película
                                                                                                                     // actualizada
                                                                                                                     // en
                                                                                                                     // el
                                                                                                                     // repositorio.

        PeliculaModel updatedPelicula = peliculaService.actualizarPelicula(1L, newDetails); // Actualiza la película con
                                                                                            // los nuevos detalles.
        assertNotNull(updatedPelicula); // Verifica que la película actualizada no sea nula.
        assertEquals("SpiderMan2", updatedPelicula.getNombrePelicula()); // Verifica que el nombre de la película
                                                                         // actualizada sea correcto.
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza parcialmente una película existente.
    // Este método actualiza parcialmente una película existente, permitiendo
    // modificar solo algunos campos.
    public void testPatch() {
        PeliculaModel existingPelicula = createPelicula(); // Crea una película existente con detalles iniciales.
        PeliculaModel patchDetails = new PeliculaModel(); // Crea un nuevo objeto PeliculaModel para los detalles del
                                                          // parche.
        patchDetails.setDuracionPelicula(169); // Establece una nueva duración para la película.

        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(existingPelicula)); // Busca una película por su
                                                                                         // ID.
        when(peliculaRepository.save(any(PeliculaModel.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Guarda
                                                                                                                     // la
                                                                                                                     // película
                                                                                                                     // actualizada
                                                                                                                     // en
                                                                                                                     // el
                                                                                                                     // repositorio.

        PeliculaModel patchedPelicula = peliculaService.actualizarPeliculaParcial(1L, patchDetails); // Actualiza la
                                                                                                     // película
                                                                                                     // parcialmente con
                                                                                                     // los detalles del
                                                                                                     // parche.
        assertNotNull(patchedPelicula); // Verifica que la película actualizada no sea nula.
        assertEquals(169, patchedPelicula.getDuracionPelicula()); // Verifica que la duración de la película se haya
                                                                  // actualizado correctamente.
        assertEquals("SpiderMan", patchedPelicula.getNombrePelicula()); // Verifica que el nombre de la película no haya
                                                                        // cambiado.
    }

// ---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testDeleteById() {
        PeliculaModel pelicula = createPelicula();

        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));

        doNothing().when(detallePedidoRepository).deleteByPelicula(pelicula);
        doNothing().when(detalleVentaRepository).deleteByPelicula(pelicula);
        doNothing().when(accesorioRepository).deleteByPelicula(pelicula);
        doNothing().when(peliculaRepository).delete(pelicula);

        peliculaService.eliminarPeliculaPorId(1L);

        verify(detallePedidoRepository, times(1)).deleteByPelicula(pelicula);
        verify(detalleVentaRepository, times(1)).deleteByPelicula(pelicula);
        verify(accesorioRepository, times(1)).deleteByPelicula(pelicula);
        verify(peliculaRepository, times(1)).delete(pelicula);
    }
// ---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testDeleteById_NoExiste() {
        when(peliculaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            peliculaService.eliminarPeliculaPorId(99L);
        });

        verify(peliculaRepository, times(0)).delete(any(PeliculaModel.class));
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
}
