package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.PersonaEntity;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    @Query("SELECT u FROM UserEntity u WHERE u.persona.idPersona = :personaId")
    Optional<UserEntity> findByPersonaId(Long personaId);

}
