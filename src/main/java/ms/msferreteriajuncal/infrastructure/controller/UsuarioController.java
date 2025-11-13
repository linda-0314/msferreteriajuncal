package ms.msferreteriajuncal.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.UsuarioQueryService;
import ms.msferreteriajuncal.application.dto.out.UsuarioPersonaResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioQueryService usuarioQueryService;

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioPersonaResponseDto>> listarUsuarios() {
        return ResponseEntity.ok(usuarioQueryService.listarUsuariosConDatos());
    }
}
