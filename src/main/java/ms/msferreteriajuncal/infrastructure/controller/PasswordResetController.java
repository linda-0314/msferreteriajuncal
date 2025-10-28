package ms.msferreteriajuncal.infrastructure.controller;

import ms.msferreteriajuncal.application.dto.in.ForgotPasswordRequestDto;
import ms.msferreteriajuncal.application.dto.in.ResetPasswordRequestDto;
import ms.msferreteriajuncal.application.port.interactor.IPasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/password")
public class PasswordResetController {

    @Autowired
    private IPasswordResetService passwordResetService;

    // POST /api/password/forgot  { "usernameOrEmail": "..." }
    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequestDto dto) {
        try {
            String token = passwordResetService.requestPasswordReset(dto.getUsernameOrEmail());
            return ResponseEntity.ok(Map.of(
                    "message", "Se generó el token de restablecimiento.",
                    "token", token
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/password/reset  { "token": "...", "newPassword": "..." }
    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequestDto dto) {
        try {
            passwordResetService.resetPassword(dto.getToken(), dto.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
