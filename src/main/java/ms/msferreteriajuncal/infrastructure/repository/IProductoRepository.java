package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.List;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;


public interface IProductoRepository extends JpaRepository<ProductoEntity, Long> {

    @Modifying
    @Query("""
        update ProductoEntity p
        set p.proCantidad = p.proCantidad - :qty
        where p.idProducto = :id and p.proCantidad >= :qty
    """)
    int descontarStock(@Param("id") Long id, @Param("qty") int qty);

    List<ProductoEntity> findTop20ByNombreProductoContainingIgnoreCaseOrderByNombreProductoAsc(String nombre);

    List<ProductoEntity> findByProCantidadLessThan(int umbral);
}

//hace la resta en la BD y solo si hay stock suficiente, y si no “Stock insuficiente”..