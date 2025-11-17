package ms.msferreteriajuncal.application;

import ms.msferreteriajuncal.application.dto.in.ProductoDto;
import ms.msferreteriajuncal.application.port.interactor.IProductoService;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import ms.msferreteriajuncal.infrastructure.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private IProductoRepository productoRepository;

    // ===========================
    // Crear / actualizar producto
    // ===========================
    @Override
    @Transactional
    public ProductoDto guardarProducto(ProductoDto dto) {
        ProductoEntity entity;

        if (dto.getIdProducto() != null) {
            // Actualizar existente
            entity = productoRepository.findById(dto.getIdProducto())
                    .orElse(new ProductoEntity());
        } else {
            // Crear nuevo
            entity = new ProductoEntity();
            entity.setProActivo(true); // por defecto activo
        }

        entity.setNombreProducto(dto.getNombreProducto());
        entity.setProCategoria(dto.getProCategoria());
        entity.setProUnidad(dto.getProUnidad());
        entity.setProCantidad(dto.getProCantidad());
        entity.setProPrecioEntrada(dto.getProPrecioEntrada());
        entity.setProPrecioSalida(dto.getProPrecioSalida());
        entity.setProDescuento(dto.getProDescuento());

        ProductoEntity guardado = productoRepository.save(entity);

        // Volvemos a DTO
        ProductoDto res = new ProductoDto();
        res.setIdProducto(guardado.getIdProducto());
        res.setNombreProducto(guardado.getNombreProducto());
        res.setProCategoria(guardado.getProCategoria());
        res.setProUnidad(guardado.getProUnidad() != null ? guardado.getProUnidad() : 0);
        res.setProCantidad(guardado.getProCantidad() != null ? guardado.getProCantidad() : 0);
        res.setProPrecioEntrada(guardado.getProPrecioEntrada() != null ? guardado.getProPrecioEntrada() : 0L);
        res.setProPrecioSalida(guardado.getProPrecioSalida() != null ? guardado.getProPrecioSalida() : 0L);
        res.setProDescuento(guardado.getProDescuento() != null ? guardado.getProDescuento() : 0L);

        return res;
    }

    // ===========================
    // Listar productos
    // ===========================
    @Override
    public List<ProductoEntity> listarProducto() {
        // 🔥 Solo productos activos para el inventario
        return productoRepository.findByProActivoTrueOrderByNombreProductoAsc();
    }

    // ===========================
    // Obtener por id
    // ===========================
    @Override
    public Optional<ProductoEntity> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    // ===========================
    // Eliminar (borrado lógico)
    // ===========================
    @Override
    @Transactional
    public void eliminarProductoPorId(Long idProducto) {
        productoRepository.findById(idProducto).ifPresent(p -> {
            // 👇 👇 AQUÍ EL TRUCO: NO HACEMOS deleteById
            p.setProActivo(false);   // lo marcamos como inactivo
            p.setProCantidad(0);     // opcional: dejas el stock en 0
            productoRepository.save(p);
        });
    }

    // ===========================
    // Buscar por nombre (para remisiones / autocompletar)
    // ===========================
    @Override
    public List<ProductoEntity> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        return productoRepository
                .findTop20ByProActivoTrueAndNombreProductoContainingIgnoreCaseOrderByNombreProductoAsc(nombre.trim());
    }
}
