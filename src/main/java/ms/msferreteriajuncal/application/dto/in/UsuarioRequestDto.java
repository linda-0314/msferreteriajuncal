package ms.msferreteriajuncal.application.dto.in;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDto {
    private String username;
    private String email;
    private String password;
    private String perNombre;
    private String perApellido;
    private String perTipoDocumento;
    private String perIdentidad;
    private String perDireccion;
    private Long idRol;
}