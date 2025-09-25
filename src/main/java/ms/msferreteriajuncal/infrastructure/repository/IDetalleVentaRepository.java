package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.application.dto.out.VentaPorProductoDTO;
import ms.msferreteriajuncal.domain.entity.DetallesVenta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IDetalleVentaRepository extends JpaRepository<DetallesVenta, Long> {

    @Query("""
      SELECT new ms.msferreteriajuncal.application.dto.out.VentaPorProductoDTO(
        d.idProducto.idProducto,
        d.idProducto.nombreProducto,
        SUM(d.cantidad),
        SUM(d.cantidad * d.precio)
      )
      FROM DetallesVenta d
      JOIN d.idVenta v
      WHERE v.fecha BETWEEN :desde AND :hasta
      GROUP BY d.idProducto.idProducto, d.idProducto.nombreProducto
      ORDER BY SUM(d.cantidad * d.precio) DESC
    """)
    List<VentaPorProductoDTO> ventasPorProducto(@Param("desde") LocalDateTime desde,
                                                @Param("hasta") LocalDateTime hasta,
                                                Pageable pageable);
}
