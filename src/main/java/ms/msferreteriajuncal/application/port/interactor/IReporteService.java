package ms.msferreteriajuncal.application.port.interactor;

import ms.msferreteriajuncal.application.dto.out.VentaResumenDto;

import java.time.LocalDate;

public interface IReporteService {

    // JSON (para la tabla de "Resumen de Ventas")
    VentaResumenDto resumenVentas(LocalDate desde, LocalDate hasta);

    // PDFs (coinciden con lo que muestra tu PdfUtil)
    byte[] pdfVentasDiarias(LocalDate desde, LocalDate hasta);
    byte[] pdfTopProductos(LocalDate desde, LocalDate hasta, int limit);
    byte[] pdfStockBajo(int umbral);
    byte[] pdfValorInventario();
}
