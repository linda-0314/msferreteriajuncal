package ms.msferreteriajuncal.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.VentaService;
import ms.msferreteriajuncal.application.dto.in.VentaCreateDTO;
import ms.msferreteriajuncal.application.dto.out.VentaDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venta")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping("/ping")
    public String ping(){ return "ok"; }

    // === Crear venta (igual al tuyo) ===
    @PostMapping
    public VentaDTO crear(@RequestBody @Validated VentaCreateDTO in ) {
        return ventaService.crear(in);
    }

    // === Obtener venta (igual al tuyo) ===
    @GetMapping("/{id}")
    public VentaDTO obtener(@PathVariable Long id) {
        return ventaService.obtenerPorId(id);
    }

    // === NUEVO: Descargar/visualizar PDF de la remisión ===
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        byte[] pdf = ventaService.generarPdfRemision(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=remision-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // === NUEVO: Listar remisiones por cliente (documento o nombre) ===
    @GetMapping("/by-cliente")
    public List<VentaDTO> porCliente(
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String nombre
    ) {
        return ventaService.buscarPorCliente(documento, nombre);
    }

    @GetMapping
    public List<VentaDTO> listar() { return ventaService.listarTodas(); }

    @GetMapping("/by-user")
    public List<VentaDTO> porUsuario(@RequestParam Long userId) {
        return ventaService.listarPorUsuario(userId);
    }

}
