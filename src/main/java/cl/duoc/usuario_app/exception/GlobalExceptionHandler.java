package cl.duoc.usuario_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> manejarRecursoNoEncontrado(
            ResourceNotFoundException ex,
            WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> manejarErroresDeValidacion(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String mensajeErrores = "Validación fallida: " + String.join(", ", errores);

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                mensajeErrores,
                request.getDescription(false)
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> manejarExcepcionGlobal(
            Exception ex,
            WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                "Error interno del servidor",
                request.getDescription(false)
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}