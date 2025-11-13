package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.UserEntity;
import ms.msferreteriajuncal.domain.entity.UserRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRolEntityRepository extends JpaRepository<UserRolEntity, Long> {

    void deleteByUserEntity(UserEntity userEntity);

    Optional<UserRolEntity> findByUserEntity(UserEntity userEntity);
}
