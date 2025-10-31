package ms.msferreteriajuncal.application.port.interactor;

import ms.msferreteriajuncal.application.dto.in.ProductoDto;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;

import java.util.List;
import java.util.Optional;

public interface IProductoService  {


    ProductoDto guardarProducto(ProductoDto productoDto); // crear o actualizar RECIBE EN DTO

    List<ProductoEntity> listarProducto();// DEVUELVE TODOS LO PRODUCTOS

    Optional<ProductoEntity> getProductoById(Long id); // BUSCA POR SU ID

    List<ProductoEntity> buscarPorNombre(String nombre);

    void eliminarProductoPorId(Long idProveedor); // ELIMINA


}
