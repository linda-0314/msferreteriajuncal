package ms.msferreteriajuncal.infrastructure.controller;

import ms.msferreteriajuncal.application.ProductoService;
import ms.msferreteriajuncal.application.ProveedorService;
import ms.msferreteriajuncal.application.dto.in.ProductoDto;
import ms.msferreteriajuncal.application.dto.in.ProveedorDto;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import ms.msferreteriajuncal.domain.entity.ProveedorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/Proveedor")

public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;


    // REGISTRAR
    @PostMapping("/guardar")
    public ResponseEntity<?> Guardar (@RequestBody ProveedorDto ProveedorDto) {
        proveedorService.guardarpro(ProveedorDto);
        return new ResponseEntity<>("proveedor registrado ", HttpStatus.CREATED);
    }

    //LISTAR
    @GetMapping("/obtener")
    public ResponseEntity<List<ProveedorEntity>> Listar(){
        List<ProveedorEntity> ProveedorEntities = proveedorService.listarProvee();
        return new ResponseEntity<>(ProveedorEntities, HttpStatus.OK);
    }

    //MODIFICAR
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDto> actualizar(@PathVariable Long id, @RequestBody ProveedorDto dto)
    {

        dto.setIdProveedor(id);
        ProveedorDto actualizado = proveedorService.guardarpro(dto);
        return ResponseEntity.ok(actualizado);
    }

    //ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // existe el producto ?
        if (proveedorService.getProveedorById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Si existe, lo eliminamos
        proveedorService.eliminarProveedorPorId(id);
        // Respondemos borrado correcto sin cuerpo
        return ResponseEntity.ok( ).build();
    }


}