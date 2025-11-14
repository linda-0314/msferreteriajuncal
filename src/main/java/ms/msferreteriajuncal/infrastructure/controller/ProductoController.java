package ms.msferreteriajuncal.infrastructure.controller;

import ms.msferreteriajuncal.application.ProductoService;
import ms.msferreteriajuncal.application.dto.in.ProductoDto;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /**
     * DTO liviano pensado para autocompletado en Remisión/Venta:
     * - id: id del producto
     * - nombre: nombre visible
     * - valorUnitario: precio de salida actual (pro_precio_salida)
     */
    public record ProductoLite(Long id, String nombre, Long valorUnitario) {}

    // =========================
    //      AUTOCOMPLETADO
    // =========================
    /**
     * Busca por nombre (insensible a mayúsculas/minúsculas) y devuelve como máximo 20 coincidencias.
     * Uso desde el frontend:
     *   GET /productos/buscar?q=tuTexto
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoLite>> buscar(@RequestParam(name = "q", required = false) String q) {
        List<ProductoLite> result = productoService.buscarPorNombre(q)
                .stream()
                .map(p -> new ProductoLite(
                        p.getIdProducto(),
                        p.getNombreProducto(),
                        p.getProPrecioSalida() == null ? 0L : p.getProPrecioSalida()
                ))
                .toList();
        return ResponseEntity.ok(result);
    }

    // crear
    @PostMapping("/guardar")
    public ResponseEntity<ProductoDto> guardar(@RequestBody ProductoDto producto) {
        ProductoDto guardado = productoService.guardarProducto(producto);
        return new ResponseEntity<>(guardado, HttpStatus.OK);
    }

    /**
     * Lista todos los productos (entidad completa).
     */
    @GetMapping("/listar")
    public ResponseEntity<List<ProductoEntity>> listar() {
        List<ProductoEntity> productos = productoService.listarProducto();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    /**
     * Obtiene un producto por id (entidad completa).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoEntity> obtenerPorId(@PathVariable Long id) {
        ProductoEntity producto = productoService.getProductoById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoDto dto
    ){
        dto.setIdProducto(id);

        ProductoDto actualizado = productoService.guardarProducto(dto);

        return ResponseEntity.ok(actualizado);
    }



    /**
     * Elimina un producto por id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (productoService.getProductoById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminarProductoPorId(id);
        return ResponseEntity.ok().build();
    }

}
