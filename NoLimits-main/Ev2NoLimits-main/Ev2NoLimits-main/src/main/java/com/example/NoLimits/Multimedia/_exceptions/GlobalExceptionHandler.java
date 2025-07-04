package com.example.NoLimits.Multimedia._exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Indica que esta clase se aplicara sobre todos los controladores del proyecto.
@ControllerAdvice
public class GlobalExceptionHandler {

    // El siguiente método se ejecutarpa automáticamente cuando ocurra una excepción de validación (Se activa cuando un @Valid falle en el controlador).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // Declara un método publico que recibe la expeción "ex".
    // Devuelve una respuesta HHTP con cuerpo tipo String.
    public ResponseEntity<String> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        // Obtiene el primer mensaje de error personalizado generado por la validación @NotNull, @Size, etc.
        // Get index 0 = accede al primer error encontrado.
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        // Devuelve una respuesta HTTP con código 400 Bad Request seguido del mensaje de validación.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de validación: " + errorMessage);
    }

    // El siguiente método se ejecutarpa automáticamente cuando ocurra una excepción de validación (Se activa cuando un @Valid falle en el controlador).
    @ExceptionHandler(RecursoNoEncontradoException.class)
    // Método que captura la excepción mencionada y arma una respuesta personalizada para el cliente.
    public ResponseEntity<String> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        // Devuelve una respuesta con código 404 y el mensaje que se definió al lanzar la excepción.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
    }

    // Este método atrapará cualquier otra excepción genérica de tipo RuntimeException que no haya sido capturada por los anteriores.
    // Sirve como respaldo para errores inesperados.
    @ExceptionHandler(RuntimeException.class)
    // Método para manejar errores genéricos en tiempo de ejecución.
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        // Devuelve un 400 Bad Request con el mensaje del error, por ejemplo:
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
    }
}