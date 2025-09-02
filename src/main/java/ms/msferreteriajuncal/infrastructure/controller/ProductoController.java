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


    // REGISTRAR
    @PostMapping("/guardar")
    public ResponseEntity<?> Guardar (@RequestBody ProductoDto productoDto) {
        productoService.guardarProducto(productoDto);
        return new ResponseEntity<>("producto registrado ", HttpStatus.CREATED);
    }

    //LISTAR
    @GetMapping("/obtener")
    public ResponseEntity<List<ProductoEntity>> Listar(){
        List<ProductoEntity> productoEntities = productoService.listarProducto();
        return new ResponseEntity<>(productoEntities, HttpStatus.OK);
    }

    //MODIFICAR
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizar(@PathVariable Long id, @RequestBody ProductoDto dto)
    {

        dto.setId(id);
        ProductoDto actualizado = productoService.guardarProducto(dto);
        return ResponseEntity.ok(actualizado);
    }

    //ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // existe el producto ?
        if (productoService.getProductoById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Si existe, lo eliminamos
        productoService.eliminarProductoPorId(id);
        // Respondemos borrado correcto sin cuerpo
        return ResponseEntity.ok( ).build();
    }


}
