package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.VentasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
// agrega:
import java.time.LocalDateTime;
import java.util.List;


public interface VentaRepository extends JpaRepository<VentasEntity,Long> {
    List<ms.msferreteriajuncal.domain.entity.VentasEntity> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta); // <-- AJUSTA "fecha" si tu campo es fechaVenta
}
