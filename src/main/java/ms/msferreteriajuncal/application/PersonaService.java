package ms.msferreteriajuncal.application;

import ms.msferreteriajuncal.application.dto.in.UsuarioRequestDto;
import ms.msferreteriajuncal.application.port.interactor.IPersonaService;
import ms.msferreteriajuncal.domain.entity.PersonaEntity;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import ms.msferreteriajuncal.domain.entity.UserRolEntity;
import ms.msferreteriajuncal.infrastructure.repository.IPersonaRepository;
import ms.msferreteriajuncal.infrastructure.repository.IUserRolEntityRepository;
import ms.msferreteriajuncal.infrastructure.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaService implements IPersonaService {


    @Autowired
    private IPersonaRepository personaRepository; // se inyecta el repositorio a la entidad perosna

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IUserRolEntityRepository userRolRepository;

    @Override
    public List<PersonaEntity> listPersonas() {
        List<PersonaEntity> personas = personaRepository.findAll();
        return personas;
        // imprime una lista de todas las personas
    }


    public Optional<PersonaEntity> getProductoById(Long idPersona) {
        Optional<PersonaEntity> personas = personaRepository.findById(idPersona);
        return personas;
        // imprime especificamente por el id
    }

    public PersonaEntity guardarPersona(UsuarioRequestDto persona) {
        PersonaEntity personaEntity = new PersonaEntity();

        personaEntity.setPerNombre(persona.getPerNombre());
        personaEntity.setPerApellido(persona.getPerApellido());
        personaEntity.setPerTipoDocumento(persona.getPerTipoDocumento());
        personaEntity.setPerIdentidad(persona.getPerIdentidad());
        personaEntity.setPerDireccion(persona.getPerDireccion());

        PersonaEntity person = personaRepository.save(personaEntity);

        return person;
    }

    public void eliminarPersona(Long idPersona) {
        personaRepository.deleteById(idPersona);
        // elimana a la persona con el id
    }

    public UserEntity guardarUser(UsuarioRequestDto loguinRequest, PersonaEntity person) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPersona(person);
        userEntity.setUsername(loguinRequest.getUsername());
        userEntity.setPassword(person.getPerIdentidad());
        userEntity.setEmail(loguinRequest.getEmail());
        userEntity.setEstadoUsuario(true);
        userEntity.setFechaActualizacion(LocalDateTime.now());
        userEntity.setFechaCreacion(LocalDateTime.now());

        UserEntity user = usuarioRepository.save(userEntity);

        return user;
    }

    public void guardarUserRol(UsuarioRequestDto loguinRequest, UserEntity user) {
        UserRolEntity userRolEntity = new UserRolEntity();

        userRolEntity.setUserEntity(user);
        userRolEntity.setId_Rol(loguinRequest.getIdRol());

        userRolRepository.save(userRolEntity);
    }

    @Override
    public void guardarUsuario(UsuarioRequestDto loguinRequest) {
        if (usuarioRepository.existsByUsername(loguinRequest.getUsername())) {
            // Usuario ya existe
        }
        if (usuarioRepository.existsByEmail(loguinRequest.getEmail())) {
            // Email ya existe
        }

        PersonaEntity person = guardarPersona(loguinRequest);

        UserEntity user = guardarUser(loguinRequest, person);

        guardarUserRol(loguinRequest, user);

    }


}