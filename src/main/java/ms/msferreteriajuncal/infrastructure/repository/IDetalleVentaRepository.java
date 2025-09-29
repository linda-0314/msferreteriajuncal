package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.DetallesVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDetalleVentaRepository  extends JpaRepository<DetallesVenta, Long> { // Para GET /ventas/{id} con detalles (opcional pero recomendado)
    @Query("select d from DetallesVenta d where d.idVenta.idVentas = :ventaId")
    List<DetallesVenta> findAllByVentaId(@Param("ventaId") Long ventaId);
}
