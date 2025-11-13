package ms.msferreteriajuncal.application.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPersonaResponseDto {
    private Long id;
    private Long personaId; // id del USUARIO (más estable en tu modelo)
    private String nombre;    // persona.perNombre
    private String apellido;  // persona.perApellido
    private String email;     // user.email
    private String documento; // persona.perIdentidad
    private String rol;       // Roles.rol_Name
    private String password;  // user.password (el front decide si enmascara)
}
