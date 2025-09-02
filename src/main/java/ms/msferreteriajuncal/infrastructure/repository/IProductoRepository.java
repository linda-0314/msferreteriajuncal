package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductoRepository extends JpaRepository<ProductoEntity, Long> {
}
