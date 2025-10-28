package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.PasswordResetTokenEntity;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByToken(String token);

    // Opción 1 (recomendada): Spring Data genera el DELETE por nombre (no hace falta @Query)
    @Modifying // marcar como operación de escritura
    void deleteByUser(UserEntity user);
}
