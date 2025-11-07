package ms.msferreteriajuncal.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import ms.msferreteriajuncal.application.dto.in.VentaCreateDTO;
import ms.msferreteriajuncal.application.dto.out.VentaDTO;
import ms.msferreteriajuncal.application.util.PdfUtil;
import ms.msferreteriajuncal.domain.entity.DetallesVenta;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import ms.msferreteriajuncal.domain.entity.VentasEntity;
import ms.msferreteriajuncal.infrastructure.repository.IDetalleVentaRepository;
import ms.msferreteriajuncal.infrastructure.repository.IProductoRepository;
import ms.msferreteriajuncal.infrastructure.repository.VentaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final IDetalleVentaRepository detalleRepo;
    private final IProductoRepository productoRepo;
    private final MailService mailService;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public VentaDTO crear(VentaCreateDTO in) {

        if (in.getItems() == null || in.getItems().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un ítem");
        }

        // 1) Cabecera
        VentasEntity venta = new VentasEntity();
        venta.setFecha(LocalDateTime.now());

        UserEntity userRef = em.getReference(UserEntity.class, in.getUserId());
        venta.setUser(userRef);

        // Datos del cliente “de mostrador”
        venta.setClienteNombre(in.getClienteNombre());
        venta.setClienteDocumento(in.getClienteDocumento());
        venta.setClienteEmail(in.getClienteEmail());

        venta = ventaRepository.save(venta);

        // 2) Detalles + stock + total
        BigDecimal total = BigDecimal.ZERO;
        for (VentaCreateDTO.Item it : in.getItems()) {
            ProductoEntity prod = em.getReference(ProductoEntity.class, it.getIdProducto());

            // tomar precio unitario
            BigDecimal precioUnit = it.getPrecioUnitario() != null
                    ? it.getPrecioUnitario()
                    : BigDecimal.valueOf(prod.getProPrecioSalida() == null ? 0 : prod.getProPrecioSalida());

            int afectadas = productoRepo.descontarStock(prod.getIdProducto(), it.getCantidad());
            if (afectadas == 0) throw new IllegalStateException("Stock insuficiente para " + prod.getNombreProducto());

            DetallesVenta d = new DetallesVenta();
            d.setIdVenta(venta);
            d.setIdProducto(prod);
            d.setCantidad(it.getCantidad());
            d.setPrecio(precioUnit);
            detalleRepo.save(d);

            total = total.add(precioUnit.multiply(BigDecimal.valueOf(it.getCantidad())));
        }

        venta.setTotal(total);
        ventaRepository.save(venta);

        // 3) Armar DTO de respuesta
        VentaDTO dto = obtenerPorId(venta.getIdVentas());

        // 4) (Opcional) enviar por correo al cliente si viene email
        if (dto.getClienteEmail() != null && !dto.getClienteEmail().isBlank()) {
            byte[] pdf = PdfUtil.remisionPdf(dto);
            String asunto = "Remisión #" + dto.getId();
            String cuerpo = "Adjuntamos la remisión de su compra. Gracias por preferirnos.";
            mailService.enviarConAdjunto(dto.getClienteEmail(), asunto, cuerpo, pdf, "remision-" + dto.getId() + ".pdf");
        }

        return dto;
    }

    public VentaDTO obtenerPorId(Long id) {
        VentasEntity v = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        List<DetallesVenta> detalles = detalleRepo.findByIdVenta_IdVentas(v.getIdVentas());

        List<VentaDTO.Item> items = detalles.stream().map(d ->
                new VentaDTO.Item(
                        d.getIdProducto().getIdProducto(),
                        d.getIdProducto().getNombreProducto(),
                        d.getCantidad(),
                        d.getPrecio(),
                        d.getPrecio().multiply(BigDecimal.valueOf(d.getCantidad()))
                )
        ).toList();

        return new VentaDTO(
                v.getIdVentas(),
                v.getFecha(),
                v.getUser() != null ? v.getUser().getId() : null,
                v.getClienteNombre(),
                v.getClienteDocumento(),
                v.getClienteEmail(),
                v.getTotal(),
                items
        );
    }

    // ====== Generar PDF para descarga en el front ======
    public byte[] generarPdfRemision(Long id) {
        VentaDTO dto = obtenerPorId(id);
        return PdfUtil.remisionPdf(dto);
    }

    // ====== Listar remisiones por cliente (doc o nombre) ======
    public List<VentaDTO> buscarPorCliente(String documento, String nombre) {
        List<VentasEntity> ventas;
        if (documento != null && !documento.isBlank()) {
            ventas = ventaRepository.findByClienteDocumento(documento);
        } else if (nombre != null && !nombre.isBlank()) {
            ventas = ventaRepository.findByClienteNombreContainingIgnoreCase(nombre);
        } else {
            throw new IllegalArgumentException("Debe enviar documento o nombre");
        }
        return ventas.stream().map(v -> obtenerPorId(v.getIdVentas())).toList();
    }

    // ====== Listar todas las remisiones (orden desc por id) ======
    @Transactional(readOnly = true)
    public List<VentaDTO> listarTodas() {
        List<VentasEntity> ventas = ventaRepository.findAll(
                Sort.by(Sort.Direction.DESC, "idVentas")
        );
        return ventas.stream()
                .map(v -> obtenerPorId(v.getIdVentas()))
                .toList();
    }

    // ====== Listar remisiones por usuario (vendedor) ======
    @Transactional(readOnly = true)
    public List<VentaDTO> listarPorUsuario(Long userId) {
        List<VentasEntity> ventas = ventaRepository.findByUser_Id(userId);
        // Orden desc por id (getIdVentas() es 'long', no requiere null-check)
        ventas.sort(Comparator.comparingLong(VentasEntity::getIdVentas).reversed());

        return ventas.stream()
                .map(v -> obtenerPorId(v.getIdVentas()))
                .toList();
    }
}
