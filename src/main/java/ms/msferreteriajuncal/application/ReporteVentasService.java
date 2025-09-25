package ms.msferreteriajuncal.application;

import ms.msferreteriajuncal.application.dto.out.*;
import ms.msferreteriajuncal.application.port.interactor.IReporteVentasService;
import ms.msferreteriajuncal.infrastructure.repository.IDetalleVentaRepository;
import ms.msferreteriajuncal.infrastructure.repository.IVentaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@Transactional(readOnly = true)
public class ReporteVentasService implements IReporteVentasService {

    private final IVentaRepository ventaRepo;
    private final IDetalleVentaRepository detalleRepo;

    public ReporteVentasService(IVentaRepository ventaRepo,
                                IDetalleVentaRepository detalleRepo) {
        this.ventaRepo = ventaRepo;
        this.detalleRepo = detalleRepo;
    }

    private LocalDateTime ini(LocalDate d){ return d.atStartOfDay(); }
    private LocalDateTime fin(LocalDate d){ return d.plusDays(1).atStartOfDay().minusNanos(1); }

    private void validarRango(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) throw new IllegalArgumentException("Fechas requeridas");
        if (hasta.isBefore(desde)) throw new IllegalArgumentException("Rango de fechas inválido");
    }

    @Override
    public List<VentaDiariaDTO> diarias(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return ventaRepo.ventasPorDia(ini(desde), fin(hasta));
    }

    @Override
    public List<VentaPorProductoDTO> porProducto(LocalDate desde, LocalDate hasta, Integer top) {
        validarRango(desde, hasta);
        var size = (top != null && top > 0) ? top : 10; // por defecto top 10
        var pageable = PageRequest.of(0, size);
        return detalleRepo.ventasPorProducto(ini(desde), fin(hasta), pageable);
    }

    @Override
    public List<VentaPorUsuarioDTO> porUsuario(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return ventaRepo.ventasPorUsuario(ini(desde), fin(hasta));
    }

    @Override
    public ReporteVentasResumenDTO resumen(LocalDate desde, LocalDate hasta, Integer top) {
        validarRango(desde, hasta);
        var diarias   = diarias(desde, hasta);
        var productos = porProducto(desde, hasta, top);
        var usuarios  = porUsuario(desde, hasta);

        var totalPeriodo = diarias.stream()
                .map(VentaDiariaDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReporteVentasResumenDTO(desde, hasta, totalPeriodo, diarias, productos, usuarios);
    }
}
