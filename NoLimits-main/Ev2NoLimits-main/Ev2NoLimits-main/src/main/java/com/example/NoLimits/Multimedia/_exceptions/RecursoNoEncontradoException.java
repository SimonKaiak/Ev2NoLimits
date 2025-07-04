package com.example.NoLimits.Multimedia._exceptions;

// Clase definida que extiende una excepción personalizada no comprobada (Uno decide cuando usarlo).
public class RecursoNoEncontradoException extends RuntimeException {
    // Mensaje de error que puedes personalizar.
    public RecursoNoEncontradoException(String mensaje) {
        // Llama al constructor de la clase padre RuntimeException pasando el mensaje.
        super(mensaje);
    }
}