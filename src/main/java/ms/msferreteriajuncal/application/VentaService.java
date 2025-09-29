package ms.msferreteriajuncal.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.dto.in.VentaCreateDTO;
import ms.msferreteriajuncal.application.dto.out.VentaDTO;
import ms.msferreteriajuncal.domain.entity.DetallesVenta;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import ms.msferreteriajuncal.domain.entity.VentasEntity;
import ms.msferreteriajuncal.infrastructure.repository.IDetalleVentaRepository;
import ms.msferreteriajuncal.infrastructure.repository.IProductoRepository;
import ms.msferreteriajuncal.infrastructure.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepo;
    private final IProductoRepository productoRepo;
    private final IDetalleVentaRepository detalleVenta;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public VentaDTO crear (VentaCreateDTO in) {

        //
        if (in.getItems() == null || in.getItems().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un ítem");
        }

        // detalle venta
        VentasEntity venta = new VentasEntity();
        venta.setFecha(LocalDateTime.now());

        // Usuario existente
        UserEntity userRef = em.getReference(UserEntity.class, in.getUserId());
        venta.setUser(userRef);

        List<DetallesVenta> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // calcular y detalles
        for (VentaCreateDTO.Item it : in.getItems()) {

            ProductoEntity prod = productoRepo.findById(it.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + it.getIdProducto()));

            Integer cantidad = it.getCantidad();
            if (cantidad == null || cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad inválida para producto " + prod.getIdProducto());
            }

            BigDecimal precioUnit = it.getPrecioUnitario() != null
                    ? it.getPrecioUnitario()
                    : BigDecimal.valueOf(prod.getProPrecioSalida());

            BigDecimal subtotal = precioUnit.multiply(BigDecimal.valueOf(cantidad));
            total = total.add(subtotal);

            DetallesVenta det = new DetallesVenta();
            det.setIdVenta(venta);          // se vuelve a setear con ID luego de guardar
            det.setIdProducto(prod);        // referencia al producto
            det.setCantidad(cantidad);
            det.setPrecio(precioUnit);

            detalles.add(det);
        }

        // Descontar stock
        for (VentaCreateDTO.Item it : in.getItems()) {
            int updated = productoRepo.descontarStock(it.getIdProducto(), it.getCantidad());
            if (updated == 0) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para producto " + it.getIdProducto()
                );
            }
        }

        //
        venta.setTotal(total);
        venta = ventaRepo.save(venta);      // ahora ya tiene id

        for (DetallesVenta d : detalles) {
            d.setIdVenta(venta);            // asegura la FK con el id generado
        }
        detalleVenta.saveAll(detalles);

        // rta
        return mapToDTO(venta, detalles);
    }

    @Transactional(readOnly = true)
    public VentaDTO obtenerPorId(Long id) {
        VentasEntity venta = ventaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + id));

        return new VentaDTO(
                venta.getIdVentas(),
                venta.getFecha(),
                venta.getUser() != null ? venta.getUser().getId() : null,
                venta.getTotal(),
                List.of()
        );
    }

    private VentaDTO mapToDTO(VentasEntity venta, List<DetallesVenta> detalles) {
        var items = detalles.stream().map(d ->
                new VentaDTO.Item(
                        d.getIdProducto().getIdProducto(),
                        d.getIdProducto().getNombreProducto(),
                        d.getCantidad(),
                        d.getPrecio(),
                        d.getPrecio().multiply(BigDecimal.valueOf(d.getCantidad()))
                )
        ).toList();

        return new VentaDTO(
                venta.getIdVentas(),
                venta.getFecha(),
                venta.getUser() != null ? venta.getUser().getId() : null,
                venta.getTotal(),
                items
        );
    }
}
