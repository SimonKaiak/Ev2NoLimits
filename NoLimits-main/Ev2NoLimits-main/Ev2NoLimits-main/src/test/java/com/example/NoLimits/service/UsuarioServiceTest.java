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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
import com.example.NoLimits.Multimedia.service.UsuarioService;

@SpringBootTest
// Activa el perfil de configuración llamado test.
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;
//---------------------------------------------------------------------------------------------------------------------------------------------------
    // Crea un usuario de ejemplo.
    // Este método crea un objeto UsuarioModel con un ID, nombre, apellidos, correo
    private UsuarioModel usuario() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("correo");
        usuario.setTelefono(123456789);
        usuario.setPassword("password");
        return usuario;
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca todos los usuarios.
    // Este método busca todos los usuarios y retorna una lista.
    public void testFindAll() { 
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario())); // Retorna una lista de usuarios.
        List<UsuarioModel> usuarios = usuarioService.findAll(); // Busca todos los usuarios.
        assertNotNull(usuarios); // Verifica que la lista de usuarios no sea nula.
        assertEquals(1, usuarios.size()); // Verifica que la lista de usuarios tenga un elemento
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca un usuario por su ID existente.
    // Este test verifica que se retorna el usuario correcto cuando el ID existe.
    public void testFindById() {
        UsuarioModel mockUsuario = usuario(); // Usa el método auxiliar

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mockUsuario));

        UsuarioModel resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Pérez", resultado.getApellidos());
        assertEquals("correo", resultado.getCorreo());
        assertEquals(123456789, resultado.getTelefono());
        assertEquals("password", resultado.getPassword());
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Busca un usuario por ID inexistente.
    // Verifica que se lance una RecursoNoEncontradoException.
    public void testFindById_NoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            usuarioService.findById(99L);
        });
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Guarda un usuario.
    // Este método guarda un usuario y retorna el usuario guardado.
    public void testSave() { 
        UsuarioModel usuario = usuario(); // Crea un nuevo usuario.
        when(usuarioRepository.save(usuario)).thenReturn(usuario); // Retorna el usuario guardado.
        UsuarioModel savedUsuario = usuarioService.save(usuario); // Guarda el usuario.
        assertNotNull(savedUsuario); // Verifica que el usuario guardado no sea nulo
        assertEquals("Juan", savedUsuario.getNombre()); // Verifica que el nombre del usuario sea correcto.
        assertEquals("Pérez", savedUsuario.getApellidos()); // Verifica que los apellidos del usuario sean correctos.
        assertEquals("correo", savedUsuario.getCorreo()); // Verifica que el correo del usuario sea correcto.
        assertEquals(123456789, savedUsuario.getTelefono()); // Verifica que el telefono del usuario sea correcto.
        assertEquals("password", savedUsuario.getPassword()); // Verifica que la contraseña del usuario sea correcta.

    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Actualiza un usuario existente.
    // Este método actualiza parcialmente los datos de un usuario existente.
    // Verifica que los cambios se reflejen correctamente y que el repositorio
    // haya guardado los datos actualizados.
    public void testPatchUsuario() {
        // Crea un usuario existente con datos iniciales.
        UsuarioModel existingUsuarioModel = usuario(); // Método auxiliar que crea un usuario con ID 1L

        // Datos que se quieren actualizar (patch).
        UsuarioModel patchData = new UsuarioModel();
        patchData.setNombre("Carlos");
        patchData.setApellidos("Gómez");
        patchData.setCorreo("nuevo_correo");
        patchData.setTelefono(987654321);
        patchData.setPassword("nueva_password");

        // Simula que el usuario existe en el repositorio.
        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUsuarioModel));

        // Simula que el repositorio guarda y devuelve el objeto actualizado.
        when(usuarioRepository.save(any(UsuarioModel.class))).thenAnswer(inv -> inv.getArgument(0));

        // Ejecuta el método del servicio para aplicar el patch.
        UsuarioModel patchedUsuario = usuarioService.patchUsuarioModel(1L, patchData);

        // Verifica que el objeto actualizado no sea nulo.
        assertNotNull(patchedUsuario);

        // Verifica que los datos se hayan actualizado correctamente.
        assertEquals("Carlos", patchedUsuario.getNombre());
        assertEquals("Gómez", patchedUsuario.getApellidos());
        assertEquals("nuevo_correo", patchedUsuario.getCorreo());
        assertEquals(987654321, patchedUsuario.getTelefono());
        assertEquals("nueva_password", patchedUsuario.getPassword());
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Elimina un usuario por su ID existente.
    // Este método verifica que si el usuario existe, se elimina correctamente.
    public void testDeleteById() {
        // Simula que el usuario con ID 1 sí existe
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // Simula que eliminar no hace nada (void)
        doNothing().when(usuarioRepository).deleteById(1L);

        // Llama al método del servicio
        usuarioService.deletebyId(1L);

        // Verifica que se llamó correctamente
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    // Intenta eliminar un usuario que no existe.
    // Verifica que se lance una RecursoNoEncontradoException.
    public void testDeleteById_NoExiste() {
        // Simula que el usuario con ID 99 NO existe
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        // Verifica que se lanza la excepción
        assertThrows(RecursoNoEncontradoException.class, () -> {
            usuarioService.deletebyId(99L);
        });

        // Verifica que NO se haya llamado al método deleteById
        verify(usuarioRepository, times(0)).deleteById(anyLong());
    }
}
