package ms.msferreteriajuncal.application;

import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.dto.out.*;
import ms.msferreteriajuncal.application.port.interactor.IReporteService;
import ms.msferreteriajuncal.application.util.PdfUtil;
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
    private final IDetalleVentaRepository detalleVentaRepository;
    private final IProductoRepository productoRepository;

    // ------------------ Resumen de ventas (JSON) ------------------
    @Override
    public VentaResumenDto resumenVentas(LocalDate desde, LocalDate hasta) {
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.atTime(23, 59, 59);

        // Ventas en el rango
        List<VentasEntity> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        int cantidadVentas = ventas.size();
        BigDecimal totalVendido = ventas.stream()
                .map(v -> v.getTotal() == null ? BigDecimal.ZERO : v.getTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Items vendidos (suma de cantidad en detalles dentro del rango)
        List<DetallesVenta> detalles = detalleVentaRepository.findByIdVenta_FechaBetween(inicio, fin);
        long itemsVendidos = detalles.stream()
                .mapToLong(d -> Optional.ofNullable(d.getCantidad()).orElse(0))
                .sum();

        VentaResumenDto dto = new VentaResumenDto();
        dto.setDesde(desde);
        dto.setHasta(hasta);
        dto.setCantidadVentas(cantidadVentas);
        dto.setItemsVendidos(itemsVendidos);
        dto.setTotalVendido(totalVendido);

        return dto;
    }

    // ------------------ Ventas/Remisiones por día (PDF) ------------------
    @Override
    public byte[] pdfVentasDiarias(LocalDate desde, LocalDate hasta) {
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.atTime(23, 59, 59);

        List<VentasEntity> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        // Agrupar por fecha (yyyy-MM-dd) y sumar total
        Map<LocalDate, BigDecimal> mapa = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getFecha().toLocalDate(),
                        Collectors.mapping(
                                v -> v.getTotal() == null ? BigDecimal.ZERO : v.getTotal(),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // Ordenado por fecha ascendente
        List<VentaDiariaDto> data = mapa.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new VentaDiariaDto(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        String titulo = String.format("Ventas / Remisiones por día (%s a %s)", desde, hasta);
        return PdfUtil.ventasDiariasPdf(data, titulo);
    }

    // ------------------ Top productos (PDF) ------------------
    @Override
    public byte[] pdfTopProductos(LocalDate desde, LocalDate hasta, int limit) {
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.atTime(23, 59, 59);

        List<DetallesVenta> detalles = detalleVentaRepository.findByIdVenta_FechaBetween(inicio, fin);

        // Agrupa por producto: suma cantidad y total (precio * cantidad)
        Map<Long, TopProductoDto> agrupado = new HashMap<>();
        for (DetallesVenta d : detalles) {
            if (d.getIdProducto() == null) continue;
            Long id = d.getIdProducto().getIdProducto();
            String nombre = safeNombreProducto(d.getIdProducto());

            long cant = Optional.ofNullable(d.getCantidad()).orElse(0);
            BigDecimal total = (d.getPrecio() == null ? BigDecimal.ZERO : d.getPrecio())
                    .multiply(BigDecimal.valueOf(cant));

            agrupado.merge(id,
                    new TopProductoDto(id, nombre, cant, total),
                    (a, b) -> new TopProductoDto(
                            a.getIdProducto(),
                            a.getNombre(),
                            a.getCantidadVendida() + b.getCantidadVendida(),
                            a.getTotalVendido().add(b.getTotalVendido())
                    ));
        }

        // Ordena por cantidad descendente y limita
        List<TopProductoDto> data = agrupado.values().stream()
                .sorted(Comparator.comparingLong(TopProductoDto::getCantidadVendida).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return PdfUtil.topProductosPdf(data, desde, hasta, limit);
    }

    // ------------------ Stock bajo (PDF) ------------------
    @Override
    public byte[] pdfStockBajo(int umbral) {
        List<ProductoEntity> productos = productoRepository.findByProCantidadLessThan(umbral);

        List<StockBajoDto> data = productos.stream()
                .map(p -> new StockBajoDto(
                        p.getIdProducto(),
                        safeNombreProducto(p),
                        Optional.ofNullable(p.getProCantidad()).orElse(0)
                ))
                .collect(Collectors.toList());

        String titulo = String.format("Productos con stock menor a %d", umbral);
        return PdfUtil.stockBajoPdf(data, titulo);
    }

    // ------------------ Valor de inventario (PDF) ------------------
    @Override
    public byte[] pdfValorInventario() {
        List<ProductoEntity> productos = productoRepository.findAll();

        long cantidadProductos = productos.size();
        BigDecimal valorTotal = productos.stream()
                .map(p -> {
                    BigDecimal precioEntrada = BigDecimal.valueOf(
                            Optional.ofNullable(p.getProPrecioEntrada()).orElse(0L)
                    );
                    BigDecimal cantidad = BigDecimal.valueOf(
                            Optional.ofNullable(p.getProCantidad()).orElse(0)
                    );
                    return precioEntrada.multiply(cantidad);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        InventarioValorDto dto = new InventarioValorDto(cantidadProductos, valorTotal);
        return PdfUtil.inventarioValorPdf(dto, "Valor del Inventario");

    }

    // ------------------ Helpers ------------------
    private String safeNombreProducto(ProductoEntity p) {
        try {
            // Tu entidad suele tener 'nombreProducto' (o similar). Si cambia el nombre, ajusta aquí UNA sola vez.
            var field = p.getClass().getDeclaredField("nombreProducto");
            field.setAccessible(true);
            Object val = field.get(p);
            return val == null ? "(sin nombre)" : val.toString();
        } catch (Exception ignored) {
            return "(sin nombre)";
        }
    }
}
