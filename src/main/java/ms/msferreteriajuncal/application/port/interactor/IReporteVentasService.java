package ms.msferreteriajuncal.application.port.interactor;

import ms.msferreteriajuncal.application.dto.out.ReporteVentasResumenDTO;
import ms.msferreteriajuncal.application.dto.out.VentaDiariaDTO;
import ms.msferreteriajuncal.application.dto.out.VentaPorProductoDTO;
import ms.msferreteriajuncal.application.dto.out.VentaPorUsuarioDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IReporteVentasService {
    List<VentaDiariaDTO> diarias(LocalDate desde, LocalDate hasta);
    List<VentaPorProductoDTO> porProducto(LocalDate desde, LocalDate hasta, Integer top);
    List<VentaPorUsuarioDTO> porUsuario(LocalDate desde, LocalDate hasta);
    ReporteVentasResumenDTO resumen(LocalDate desde, LocalDate hasta, Integer top);
}