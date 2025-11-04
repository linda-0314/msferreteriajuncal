package ms.msferreteriajuncal.application.port.interactor;

import ms.msferreteriajuncal.application.dto.out.*;
import java.time.LocalDate;
import java.util.List;

public interface IReporteService {
    List<VentaDiariaDto> ventasDiarias(LocalDate desde, LocalDate hasta);
    VentaResumenDto resumen(LocalDate desde, LocalDate hasta);
    List<TopProductoDto> topProductos(LocalDate desde, LocalDate hasta, int limit);
    List<StockBajoDto> stockBajo(int umbral);
    InventarioValorDto valorInventario();
}
