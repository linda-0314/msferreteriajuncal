package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.application.dto.out.VentaDiariaDTO;
import ms.msferreteriajuncal.application.dto.out.VentaPorUsuarioDTO;
import ms.msferreteriajuncal.domain.entity.VentasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IVentaRepository extends JpaRepository<VentasEntity, Long> {

    @Query("""
      SELECT new ms.msferreteriajuncal.application.dto.out.VentaDiariaDTO(
        FUNCTION('date', v.fecha),
        SUM(d.cantidad * d.precio)
      )
      FROM VentasEntity v
      JOIN DetallesVenta d ON d.idVenta = v
      WHERE v.fecha BETWEEN :desde AND :hasta
      GROUP BY FUNCTION('date', v.fecha)
      ORDER BY FUNCTION('date', v.fecha)
    """)
    List<VentaDiariaDTO> ventasPorDia(@Param("desde") LocalDateTime desde,
                                      @Param("hasta") LocalDateTime hasta);

    @Query("""
      SELECT new ms.msferreteriajuncal.application.dto.out.VentaPorUsuarioDTO(
        u.id,
        u.username,
        COUNT(v),
        COALESCE(SUM(d.cantidad * d.precio), 0)
      )
      FROM VentasEntity v
      JOIN v.user u
      JOIN DetallesVenta d ON d.idVenta = v
      WHERE v.fecha BETWEEN :desde AND :hasta
      GROUP BY u.id, u.username
      ORDER BY COALESCE(SUM(d.cantidad * d.precio), 0) DESC
    """)
    List<VentaPorUsuarioDTO> ventasPorUsuario(@Param("desde") LocalDateTime desde,
                                              @Param("hasta") LocalDateTime hasta);
}
