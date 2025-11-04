package ms.msferreteriajuncal.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.dto.out.InventarioValorDto;
import ms.msferreteriajuncal.application.dto.out.StockBajoDto;
import ms.msferreteriajuncal.application.dto.out.TopProductoDto;
import ms.msferreteriajuncal.application.dto.out.VentaDiariaDto;
import ms.msferreteriajuncal.application.dto.out.VentaResumenDto;
import ms.msferreteriajuncal.application.port.interactor.IReporteService;
import ms.msferreteriajuncal.application.util.PdfUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final IReporteService reporteService;

    // ===== Ventas diarias (JSON) =====
    @GetMapping("/ventas/diario")
    public List<VentaDiariaDto> ventasDiario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return reporteService.ventasDiarias(desde, hasta);
    }

    // ===== Ventas diarias (PDF) =====
    @GetMapping(value = "/ventas/diario/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> ventasDiarioPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        List<VentaDiariaDto> data = reporteService.ventasDiarias(desde, hasta);
        byte[] pdf = PdfUtil.ventasDiariasPdf(
                data,
                "Ventas / Remisiones por día (" + desde + " a " + hasta + ")"
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ventas_diario_" + desde + "_" + hasta + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ===== Resumen de ventas (JSON) =====
    @GetMapping("/ventas/resumen")
    public VentaResumenDto resumen(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return reporteService.resumen(desde, hasta);
    }

    // ===== Resumen de ventas (PDF) =====
    @GetMapping(value = "/ventas/resumen/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> resumenVentasPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        VentaResumenDto data = reporteService.resumen(desde, hasta);
        byte[] pdf = PdfUtil.resumenVentasPdf(
                data,
                "Resumen de Ventas (" + desde + " a " + hasta + ")"
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resumen_ventas_" + desde + "_" + hasta + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ===== Top productos (JSON) =====
    @GetMapping("/top-productos")
    public List<TopProductoDto> topProductos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "10") int limit) {
        return reporteService.topProductos(desde, hasta, limit);
    }

    // ===== Top productos (PDF) =====
    @GetMapping(value = "/top-productos/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> topProductosPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<TopProductoDto> data = reporteService.topProductos(desde, hasta, limit);
        byte[] pdf = PdfUtil.topProductosPdf(data, desde, hasta, limit);

        String fileName = "top_productos_" + desde + "_a_" + hasta + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ===== Stock bajo (JSON) =====
    // Nota: dejamos la ruta JSON como ya la tenías
    @GetMapping("/inventario/stock-bajo")
    public List<StockBajoDto> stockBajo(@RequestParam(defaultValue = "10") int umbral) {
        return reporteService.stockBajo(umbral);
    }

    // ===== Stock bajo (PDF) =====
    // Ruta pedida en Postman: /reportes/stock-bajo/pdf
    @GetMapping(value = "/stock-bajo/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> stockBajoPdf(@RequestParam(defaultValue = "10") int umbral) {
        List<StockBajoDto> data = reporteService.stockBajo(umbral);
        byte[] pdf = PdfUtil.stockBajoPdf(data, "Productos con stock menor a " + umbral);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock_bajo_umbral_" + umbral + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ===== Valor inventario (JSON) =====
    @GetMapping("/inventario/valor")
    public InventarioValorDto valorInventario() {
        return reporteService.valorInventario();
    }

    // ===== Valor inventario (PDF) =====
    @GetMapping(value = "/inventario/valor/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> inventarioValorPdf() {
        InventarioValorDto data = reporteService.valorInventario();
        byte[] pdf = PdfUtil.inventarioValorPdf(data, "Valor del Inventario");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=valor_inventario.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
