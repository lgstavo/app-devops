package br.ufscar.dc.dsw.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Esta anotação torna a classe um "supervisor" global para os controllers
@ControllerAdvice
public class GlobalExceptionHandler {

    // Este método será chamado sempre que uma RuntimeException for lançada por um controller
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        // Verificamos se a mensagem é a de usuário duplicado
        if (ex.getMessage().contains("usuário já está em uso")) {
            // Se for, retornamos 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }

        // Para todas as outras RuntimeExceptions, retornamos um erro genérico 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno.");
    }
}