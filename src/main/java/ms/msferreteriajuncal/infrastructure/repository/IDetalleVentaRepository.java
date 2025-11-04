package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.DetallesVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IDetalleVentaRepository extends JpaRepository<DetallesVenta, Long> {

    // Para reportes por rango de fechas (navega: DetallesVenta.idVenta.fecha)
    List<DetallesVenta> findByIdVenta_FechaBetween(LocalDateTime desde, LocalDateTime hasta);

    // Útil si alguna vez quieres buscar los detalles por el ID de la venta
    // OJO: la PK en VentasEntity se llama idVentas
    List<DetallesVenta> findByIdVenta_IdVentas(Long idVentas);
}
