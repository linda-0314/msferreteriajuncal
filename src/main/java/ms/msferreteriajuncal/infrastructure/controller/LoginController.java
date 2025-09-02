package ms.msferreteriajuncal.infrastructure.controller;

import ms.msferreteriajuncal.application.dto.in.LoginRequestDto;
import ms.msferreteriajuncal.application.port.interactor.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    // Esto lo realizo mi primo camilo, yo no hice nada
    @PostMapping(value = "/prueba")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            loginService.login(loginRequestDto);
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
