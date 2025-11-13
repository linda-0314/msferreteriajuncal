package ms.msferreteriajuncal.application.port.interactor;

import ms.msferreteriajuncal.application.dto.in.UsuarioRequestDto;
import ms.msferreteriajuncal.domain.entity.PersonaEntity;

import java.util.List;

public interface IPersonaService {

    void guardarUsuario(UsuarioRequestDto loguinRequest);

    List<PersonaEntity> listPersonas();

    void eliminarPersonaCascade(Long personaId);
}
