package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.VentasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<VentasEntity, Long> {
    // ya usas esto en ReporteService
    List<VentasEntity> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);

    // para listar remisiones por cliente
    List<VentasEntity> findByClienteDocumento(String clienteDocumento);
    List<VentasEntity> findByClienteNombreContainingIgnoreCase(String clienteNombre);

    // por vendedor (si quieres filtrar por el user que hace la venta)
    List<VentasEntity> findByUser_Id(Long userId);
}
