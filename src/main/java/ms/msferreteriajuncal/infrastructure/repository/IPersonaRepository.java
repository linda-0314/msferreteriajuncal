package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonaRepository extends JpaRepository <PersonaEntity, Long> {

}
