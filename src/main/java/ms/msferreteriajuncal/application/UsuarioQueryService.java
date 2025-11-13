package ms.msferreteriajuncal.application;

import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.dto.out.UsuarioPersonaResponseDto;
import ms.msferreteriajuncal.domain.entity.PersonaEntity;
import ms.msferreteriajuncal.domain.entity.RolesEntity;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import ms.msferreteriajuncal.domain.entity.UserRolEntity;
import ms.msferreteriajuncal.infrastructure.repository.IRolesRepository;
import ms.msferreteriajuncal.infrastructure.repository.IUserRolEntityRepository;
import ms.msferreteriajuncal.infrastructure.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioQueryService {

    private final IUsuarioRepository usuarioRepository;
    private final IUserRolEntityRepository userRolRepository;
    private final IRolesRepository rolesRepository;

    public List<UsuarioPersonaResponseDto> listarUsuariosConDatos() {
        List<UserEntity> users = usuarioRepository.findAll();
        List<UsuarioPersonaResponseDto> out = new ArrayList<>();

        for (UserEntity u : users) {
            PersonaEntity p = u.getPersona(); // puede ser null
            Long personaId = (p != null) ? p.getIdPersona() : null;

            // Resolver nombre del rol por usuarios_roles -> roles
            String rolName = null;
            Optional<UserRolEntity> urOpt = userRolRepository.findByUserEntity(u);
            if (urOpt.isPresent()) {
                long idRol = urOpt.get().getId_Rol();
                Optional<RolesEntity> rolOpt = rolesRepository.findById(idRol);
                if (rolOpt.isPresent()) {
                    rolName = rolOpt.get().getRol_Name();
                }
            }

            UsuarioPersonaResponseDto dto = new UsuarioPersonaResponseDto();
            dto.setId(u.getId());
            dto.setPersonaId(personaId);                           // <-- NUEVO
            dto.setNombre(p != null ? p.getPerNombre()    : null);
            dto.setApellido(p != null ? p.getPerApellido() : null);
            dto.setEmail(u.getEmail());
            dto.setDocumento(p != null ? p.getPerIdentidad() : null);
            dto.setRol(rolName);
            dto.setPassword(u.getPassword());

            out.add(dto);
        }
        return out;
    }
}
