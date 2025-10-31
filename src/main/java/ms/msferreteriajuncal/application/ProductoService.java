package ms.msferreteriajuncal.application;

import ms.msferreteriajuncal.application.dto.in.ProductoDto;
import ms.msferreteriajuncal.application.port.interactor.IProductoService;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import ms.msferreteriajuncal.infrastructure.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private IProductoRepository productoRepository; // INYECTA REPOSITORIO

    @Override
    public List<ProductoEntity> listarProducto() {  //LISTAR
        return productoRepository.findAll();
    }

    @Override
    public Optional<ProductoEntity> getProductoById(Long id) {  // POR
        return productoRepository.findById(id);
    }

    @Override
    public ProductoDto guardarProducto(ProductoDto producto) {
        ProductoEntity productoEntity;

        if (producto.getId() != 0 && productoRepository.existsById(producto.getId())) {
            // Si existe, SE PUEDE MODIFICAR
            productoEntity = productoRepository.findById(producto.getId()).get();
        } else {
            // Si no existe, lo creamos
            productoEntity = new ProductoEntity();
        }


        productoEntity.setNombreProducto(producto.getNombreProducto());
        productoEntity.setProCategoria(producto.getProCategoria());
        productoEntity.setProUnidad(producto.getProUnidad());
        productoEntity.setProCantidad(producto.getProCantidad());
        productoEntity.setProPrecioEntrada(producto.getProPrecioEntrada());
        productoEntity.setProPrecioSalida(producto.getProPrecioSalida());
        productoEntity.setProDescuento(producto.getProDescuento());

        ProductoEntity savedProducto = productoRepository.save(productoEntity);
        producto.setId(savedProducto.getIdProducto());
        return producto;
    }

    @Override
    public void eliminarProductoPorId(Long idProducto) {
        productoRepository.deleteById(idProducto);
    }

    @Override
    public List<ProductoEntity> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        // Usa el que corresponda según tu Entity:
        return productoRepository.findTop20ByNombreProductoContainingIgnoreCaseOrderByNombreProductoAsc(nombre.trim());
    }

}
