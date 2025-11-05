package ms.msferreteriajuncal.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.dto.out.VentaResumenDto;
import ms.msferreteriajuncal.application.port.interactor.IReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final IReporteService reporteService;

    // --------- 1) Resumen de ventas (JSON) ----------
    @GetMapping("/ventas/resumen")
    public ResponseEntity<VentaResumenDto> resumenVentas(
            @RequestParam String desde,
            @RequestParam String hasta
    ) {
        LocalDate d = LocalDate.parse(desde);
        LocalDate h = LocalDate.parse(hasta);
        return ResponseEntity.ok(reporteService.resumenVentas(d, h));
    }

    // --------- 2) Ventas diarias (PDF) ----------
    @GetMapping("/ventas/diario")
    public ResponseEntity<byte[]> ventasDiariasPdf(
            @RequestParam String desde,
            @RequestParam String hasta
    ) {
        LocalDate d = LocalDate.parse(desde);
        LocalDate h = LocalDate.parse(hasta);
        byte[] pdf = reporteService.pdfVentasDiarias(d, h);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=ventas_diario_%s_%s.pdf", d, h));
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    // --------- 3) Top productos (PDF) ----------
    @GetMapping("/top-productos")
    public ResponseEntity<byte[]> topProductosPdf(
            @RequestParam String desde,
            @RequestParam String hasta,
            @RequestParam(defaultValue = "10") int limit
    ) {
        LocalDate d = LocalDate.parse(desde);
        LocalDate h = LocalDate.parse(hasta);
        byte[] pdf = reporteService.pdfTopProductos(d, h, limit);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=top_productos.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    // --------- 4) Stock bajo (PDF) ----------
    @GetMapping("/stock-bajo")
    public ResponseEntity<byte[]> stockBajoPdf(
            @RequestParam(defaultValue = "5") int umbral
    ) {
        byte[] pdf = reporteService.pdfStockBajo(umbral);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=stock_bajo.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    // --------- 5) Valor del inventario (PDF) ----------
    @GetMapping("/valor-inventario")
    public ResponseEntity<byte[]> valorInventarioPdf() {
        byte[] pdf = reporteService.pdfValorInventario();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=valor_inventario.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
