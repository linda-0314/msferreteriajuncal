package ms.msferreteriajuncal.application;

import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.dto.out.*;
import ms.msferreteriajuncal.application.port.interactor.IReporteService;
import ms.msferreteriajuncal.domain.entity.DetallesVenta;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import ms.msferreteriajuncal.domain.entity.VentasEntity;
import ms.msferreteriajuncal.infrastructure.repository.IDetalleVentaRepository;
import ms.msferreteriajuncal.infrastructure.repository.IProductoRepository;
import ms.msferreteriajuncal.infrastructure.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService implements IReporteService {

    private final VentaRepository ventaRepository;
    private final IDetalleVentaRepository detalleRepository;
    private final IProductoRepository productoRepository;

    private static LocalDateTime atStart(LocalDate d){ return d.atStartOfDay(); }
    private static LocalDateTime atEnd(LocalDate d){ return d.atTime(23,59,59); }

    @Override
    public List<VentaDiariaDto> ventasDiarias(LocalDate desde, LocalDate hasta) {
        // Ventas dentro del rango
        List<VentasEntity> ventas = ventaRepository.findByFechaBetween(atStart(desde), atEnd(hasta));
        Map<LocalDate, BigDecimal> mapa = new TreeMap<>();

        for (VentasEntity v : ventas) {
            LocalDate dia = v.getFecha().toLocalDate();
            BigDecimal totalVenta = v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO;
            mapa.merge(dia, totalVenta, BigDecimal::add);
        }

        return mapa.entrySet().stream()
                .map(e -> new VentaDiariaDto(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public VentaResumenDto resumen(LocalDate desde, LocalDate hasta) {
        List<VentasEntity> ventas = ventaRepository.findByFechaBetween(atStart(desde), atEnd(hasta));
        long cantidadVentas = ventas.size();
        long cantidadItems = 0;
        BigDecimal total = BigDecimal.ZERO;

        for (VentasEntity v : ventas) {
            total = total.add(v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO);
        }

        // Para contar ítems, consultamos detalles
        List<DetallesVenta> detalles = detalleRepository.findByIdVenta_FechaBetween(atStart(desde), atEnd(hasta));
        for (DetallesVenta d : detalles) {
            cantidadItems += d.getCantidad();
        }

        return new VentaResumenDto(desde, hasta, cantidadVentas, cantidadItems, total);
    }

    @Override
    public List<TopProductoDto> topProductos(LocalDate desde, LocalDate hasta, int limit) {
        List<DetallesVenta> detalles = detalleRepository.findByIdVenta_FechaBetween(atStart(desde), atEnd(hasta));
        Map<Long, TopProductoDto> acumulado = new HashMap<>();

        for (DetallesVenta d : detalles) {
            ProductoEntity p = d.getIdProducto();
            if (p == null) continue;

            Long id = p.getIdProducto();
            String nombre = p.getNombreProducto();
            BigDecimal precio = d.getPrecio() != null ? d.getPrecio() : BigDecimal.ZERO;

            TopProductoDto actual = acumulado.getOrDefault(id, new TopProductoDto(id, nombre, 0, BigDecimal.ZERO));

            long nuevaCant = actual.getCantidadVendida() + d.getCantidad();
            BigDecimal nuevoTotal = actual.getTotalVendido()
                    .add(precio.multiply(BigDecimal.valueOf(d.getCantidad())));

            acumulado.put(id, new TopProductoDto(id, nombre, nuevaCant, nuevoTotal));
        }

        return acumulado.values().stream()
                .sorted(Comparator.comparingLong(TopProductoDto::getCantidadVendida).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockBajoDto> stockBajo(int umbral) {
        List<ProductoEntity> productos = productoRepository.findByProCantidadLessThan(umbral);
        return productos.stream()
                .map(p -> new StockBajoDto(p.getIdProducto(), p.getNombreProducto(), p.getProCantidad()))
                .collect(Collectors.toList());
    }

    @Override
    public InventarioValorDto valorInventario() {
        List<ProductoEntity> productos = productoRepository.findAll();
        long cantidadProductos = productos.size();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ProductoEntity p : productos) {
            BigDecimal precioEntrada = p.getProPrecioEntrada() != null
                    ? BigDecimal.valueOf(p.getProPrecioEntrada())
                    : BigDecimal.ZERO;
            BigDecimal subtotal = BigDecimal.valueOf(p.getProCantidad()).multiply(precioEntrada);
            valorTotal = valorTotal.add(subtotal);
        }

        return new InventarioValorDto(cantidadProductos, valorTotal);
    }
}
