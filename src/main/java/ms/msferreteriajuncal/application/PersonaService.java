package ms.msferreteriajuncal.application;

import jakarta.transaction.Transactional;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonaService implements IPersonaService {

    @Autowired
    private IPersonaRepository personaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IUserRolEntityRepository userRolRepository;

    @Override
    public List<PersonaEntity> listPersonas() {
        return personaRepository.findAll();
    }

    public Optional<PersonaEntity> getProductoById(Long idPersona) {
        return personaRepository.findById(idPersona);
    }

    public PersonaEntity guardarPersona(UsuarioRequestDto persona) {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setPerNombre(persona.getPerNombre());
        personaEntity.setPerApellido(persona.getPerApellido());
        // Si no envías perTipoDocumento desde el front, quedará null y no pasa nada
        personaEntity.setPerTipoDocumento(persona.getPerTipoDocumento());
        personaEntity.setPerIdentidad(persona.getPerIdentidad());
        personaEntity.setPerDireccion(persona.getPerDireccion());
        return personaRepository.save(personaEntity);
    }

    public void eliminarPersona(Long idPersona) {
        personaRepository.deleteById(idPersona);
    }

    public UserEntity guardarUser(UsuarioRequestDto loguinRequest, PersonaEntity person) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPersona(person);
        userEntity.setUsername(loguinRequest.getUsername());
        // ¡OJO! Aquí actualmente guardas la cédula como password:
        userEntity.setPassword(person.getPerIdentidad());
        userEntity.setEmail(loguinRequest.getEmail());
        userEntity.setEstadoUsuario(true);
        userEntity.setFechaActualizacion(LocalDateTime.now());
        userEntity.setFechaCreacion(LocalDateTime.now());
        return usuarioRepository.save(userEntity);
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
            // Puedes lanzar una excepción 409 si quieres
        }
        if (usuarioRepository.existsByEmail(loguinRequest.getEmail())) {
            // Email ya existe
        }
        PersonaEntity person = guardarPersona(loguinRequest);
        UserEntity user = guardarUser(loguinRequest, person);
        guardarUserRol(loguinRequest, user);
    }

    @Transactional
    @Override
    public void eliminarPersonaCascade(Long personaId) {
        PersonaEntity persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new NoSuchElementException("Persona no encontrada"));

        // buscar el usuario dueño de esta persona
        UserEntity user = usuarioRepository.findByPersonaId(personaId).orElse(null);

        if (user != null) {
            // 1) borrar roles del usuario
            userRolRepository.deleteByUserEntity(user);
            // 2) borrar usuario
            usuarioRepository.delete(user);
        }

        // 3) borrar persona
        personaRepository.delete(persona);
    }
}
