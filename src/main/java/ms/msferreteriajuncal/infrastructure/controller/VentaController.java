package ms.msferreteriajuncal.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.VentaService;
import ms.msferreteriajuncal.application.dto.in.VentaCreateDTO;
import ms.msferreteriajuncal.application.dto.out.VentaDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/venta")
@RequiredArgsConstructor


public class VentaController {

    private final VentaService ventaService;

    @GetMapping("/ping")
    public String ping(){ return "ok"; } //


    // crear venta
    @PostMapping
    public VentaDTO crear(@RequestBody @Validated VentaCreateDTO in ) {
        return ventaService.crear(in);
    }

    @GetMapping("/{id}")
    public VentaDTO obtener(@PathVariable Long id) {
        return ventaService.obtenerPorId(id);
}
}
