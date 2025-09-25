package ms.msferreteriajuncal.infrastructure.controller;

import ms.msferreteriajuncal.application.dto.out.*;
import ms.msferreteriajuncal.application.port.interactor.IReporteVentasService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes/ventas")
public class ReporteVentasController {

    private final IReporteVentasService service;

    public ReporteVentasController(IReporteVentasService service) {
        this.service = service;
    }

    @GetMapping("/diarias")
    public ResponseEntity<List<VentaDiariaDTO>> diarias(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        if (hasta.isBefore(desde)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.diarias(desde, hasta));
    }
    @GetMapping("/por-producto")
    public ResponseEntity<List<VentaPorProductoDTO>> porProducto(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Integer top
    ) {
        if (hasta.isBefore(desde)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.porProducto(desde, hasta, top));
    }
    @GetMapping("/por-usuario")
    public ResponseEntity<List<VentaPorUsuarioDTO>> porUsuario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        if (hasta.isBefore(desde)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.porUsuario(desde, hasta));
    }

    @GetMapping("/resumen")
    public ResponseEntity<ReporteVentasResumenDTO> resumen(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Integer top
    ) {
        if (hasta.isBefore(desde)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.resumen(desde, hasta, top));
    }
}
