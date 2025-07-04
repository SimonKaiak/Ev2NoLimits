package com.example.NoLimits.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.TipoProductoModel;
import com.example.NoLimits.Multimedia.repository.TipoProductoRepository;
import com.example.NoLimits.Multimedia.service.TipoProductoService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class TipoProductoServiceTest {
    @Autowired
    private TipoProductoService tipoProductoService;

    @MockBean
    private TipoProductoRepository tipoProductoRepository;

    private TipoProductoModel tipoProducto() {
        TipoProductoModel tipoProducto = new TipoProductoModel();
        tipoProducto.setIdTipoProducto(1L);
        tipoProducto.setNombreTipoProducto("Tipo Producto 1");
        tipoProducto.setPeliculas(null); // Asignar null o una lista vacía según sea necesario
        tipoProducto.setAccesorios(null); // Asignar null o una lista vacía
        tipoProducto.setVideojuegos(null); // Asignar null o una lista vacía
        return tipoProducto;
    }

    @Test
    public void testFindAll() {
        when(tipoProductoRepository.findAll()).thenReturn(List.of(tipoProducto()));
        List<TipoProductoModel> tiposProductos = tipoProductoService.obtenerTiposProductos();
        assertNotNull(tiposProductos);
        assertEquals(1, tiposProductos.size());
        assertEquals("Tipo Producto 1", tiposProductos.get(0).getNombreTipoProducto());
    }

    @Test
    public void testFindById() {
        when(tipoProductoRepository.findById(1L)).thenReturn(java.util.Optional.of(tipoProducto()));
        TipoProductoModel tipoProducto = tipoProductoService.obtenerTipoProductoPorId(1L);
        assertNotNull(tipoProducto);
        assertEquals("Tipo Producto 1", tipoProducto.getNombreTipoProducto());
        assertEquals(1L, tipoProducto.getIdTipoProducto());
        assertEquals(null, tipoProducto.getPeliculas()); // Verifica que las listas sean nulas o vacías
        assertEquals(null, tipoProducto.getAccesorios());
        assertEquals(null, tipoProducto.getVideojuegos());
    }

    @Test
    public void testSave() {
        when(tipoProductoRepository.save(any(TipoProductoModel.class))).thenReturn(tipoProducto());
        TipoProductoModel tipoProducto = tipoProductoService.guardarTipoProducto(tipoProducto());
        assertNotNull(tipoProducto);
        assertEquals("Tipo Producto 1", tipoProducto.getNombreTipoProducto());
        assertEquals(1L, tipoProducto.getIdTipoProducto());
        assertEquals(null, tipoProducto.getPeliculas()); // Verifica que las listas sean nulas o vacías
        assertEquals(null, tipoProducto.getAccesorios());
        assertEquals(null, tipoProducto.getVideojuegos());
    }

    @Test
    public void testPatchTestTipoProducto() {
        TipoProductoModel tipoProductoExistente = tipoProducto();
        TipoProductoModel patchData = new TipoProductoModel();
        patchData.setIdTipoProducto(1L);
        patchData.setNombreTipoProducto("Nuevo Tipo Producto");
        patchData.setPeliculas(null); // Asignar null o una lista vacía según sea necesario
        patchData.setAccesorios(null); // Asignar null o una lista vacía
        patchData.setVideojuegos(null); // Asignar null o una lista vacía

        when(tipoProductoRepository.findById(1L)).thenReturn(java.util.Optional.of(tipoProductoExistente));
        when(tipoProductoRepository.save(any(TipoProductoModel.class))).thenReturn(patchData);

        TipoProductoModel tipoProductoActualizado = tipoProductoService.actualizarTipoProductoParcial(1L, patchData);
        assertNotNull(tipoProductoActualizado);
        assertEquals("Nuevo Tipo Producto", tipoProductoActualizado.getNombreTipoProducto());
        assertEquals(1L, tipoProductoActualizado.getIdTipoProducto());
        assertEquals(null, tipoProductoActualizado.getPeliculas()); // Verifica que las listas
        assertEquals(null, tipoProductoActualizado.getAccesorios());
        assertEquals(null, tipoProductoActualizado.getVideojuegos());
    }

    @Test
    // Elimina un tipo de producto existente.
    // Este método prueba que cuando el tipo de producto existe, se elimina correctamente.
    // Verifica también que se haya llamado al método delete(...) del repositorio.
    public void testEliminarTipoProducto_SiExiste() {
        // Crea un tipo de producto de ejemplo con listas vacías de relaciones.
        TipoProductoModel tipoProducto = new TipoProductoModel();
        tipoProducto.setIdTipoProducto(1L);
        tipoProducto.setNombreTipoProducto("Consolas");
        tipoProducto.setAccesorios(new ArrayList<>());
        tipoProducto.setPeliculas(new ArrayList<>());
        tipoProducto.setVideojuegos(new ArrayList<>());

        // Simula que el tipo de producto existe en la base de datos.
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(tipoProducto));

        // Ejecuta el método del servicio para eliminar.
        tipoProductoService.eliminarTipoProducto(1L);

        // Verifica que el repositorio haya llamado a delete con el objeto correcto.
        verify(tipoProductoRepository, times(1)).delete(tipoProducto);
    }

    @Test
    // Intenta eliminar un tipo de producto que no existe.
    // Este test verifica que se lance una excepción RecursoNoEncontradoException si el ID no está en la base de datos.
    // También se asegura de que NO se llame al método delete(...) del repositorio.
    public void testEliminarTipoProducto_NoExiste() {
        // Simula que el tipo de producto con ID 99 no existe.
        when(tipoProductoRepository.findById(99L)).thenReturn(Optional.empty());

        // Verifica que se lance la excepción esperada al intentar eliminar.
        assertThrows(RecursoNoEncontradoException.class, () -> {
            tipoProductoService.eliminarTipoProducto(99L);
        });

        // Verifica que no se haya llamado a delete si no se encontró el tipo de producto.
        verify(tipoProductoRepository, times(0)).delete(any(TipoProductoModel.class));
    }

    
}