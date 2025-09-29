package ms.msferreteriajuncal.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleIAE(HttpServletRequest req, IllegalArgumentException ex) {
        // Usa 404 si el mensaje contiene "no encontrado"
        int status = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("no encontrado") ? 404 : 409;
        return body(status, ex.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(HttpServletRequest req, MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst().orElse("Solicitud inválida");
        return body(400, msg, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneric(HttpServletRequest req, Exception ex) {
        // Opcional: log detallado en consola
        ex.printStackTrace();
        return body(500, ex.getClass().getSimpleName() + ": " + (ex.getMessage()==null?"":ex.getMessage()), req);
    }

    private ResponseEntity<Map<String,Object>> body(int status, String message, HttpServletRequest req) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("timestamp", OffsetDateTime.now().toString());
        m.put("status", status);
        m.put("message", message);
        m.put("path", req.getRequestURI());
        return ResponseEntity.status(status).body(m);
    }
}
