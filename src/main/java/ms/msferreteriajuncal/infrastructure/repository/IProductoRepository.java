package ms.msferreteriajuncal.infrastructure.repository;

import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductoRepository extends JpaRepository<ProductoEntity, Long> {

    @Modifying
    @Query("""
        update ProductoEntity p
        set p.proCantidad = p.proCantidad - :qty
        where p.idProducto = :id and p.proCantidad >= :qty
    """)
    int descontarStock(@Param("id") Long id, @Param("qty") int qty);

    List<ProductoEntity> findByProActivoTrueOrderByNombreProductoAsc();

    List<ProductoEntity> findTop20ByProActivoTrueAndNombreProductoContainingIgnoreCaseOrderByNombreProductoAsc(String nombre);

    // Ya lo tenías: productos con stock por debajo de un umbral
    List<ProductoEntity> findByProCantidadLessThan(int umbral);
}
