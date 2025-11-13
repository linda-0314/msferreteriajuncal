package ms.msferreteriajuncal.infrastructure.controller;
import ms.msferreteriajuncal.application.dto.in.UsuarioRequestDto;
import ms.msferreteriajuncal.application.port.interactor.IPersonaService;
import ms.msferreteriajuncal.domain.entity.PersonaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/prueba")
public class PersonaController {

    @Autowired
    private IPersonaService personaService;


    @PostMapping("/guardar/usuario")
    public ResponseEntity<?> Guardar (@RequestBody UsuarioRequestDto persona) {
        personaService.guardarUsuario(persona);
        return new ResponseEntity<>("Usuario ok", HttpStatus.OK);
    }


    @GetMapping("listar/persona")
    public ResponseEntity<List<PersonaEntity>> listarPersonas() {
        List<PersonaEntity> personaEntities = personaService.listPersonas();
        return new ResponseEntity<>(personaEntities, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/persona/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable("id") Long id) {
        personaService.eliminarPersonaCascade(id);
        return ResponseEntity.noContent().build();
    }

}



