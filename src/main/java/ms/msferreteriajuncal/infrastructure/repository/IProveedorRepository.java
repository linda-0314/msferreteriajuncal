package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.application.dto.in.ProveedorDto;
import ms.msferreteriajuncal.domain.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProveedorRepository extends JpaRepository<ProveedorEntity, Long> {
}
