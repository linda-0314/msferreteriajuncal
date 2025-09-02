package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.UserRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRolEntityRepository extends JpaRepository<UserRolEntity, Long> {

}
