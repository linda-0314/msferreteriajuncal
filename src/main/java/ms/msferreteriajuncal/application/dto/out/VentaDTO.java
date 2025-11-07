package ms.msferreteriajuncal.application.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VentaDTO {
    private Long id;
    private LocalDateTime fecha;

    private Long userId;          // vendedor
    private String clienteNombre; // NUEVO
    private String clienteDocumento; // NUEVO
    private String clienteEmail;  // NUEVO

    private BigDecimal total;

    private List<Item> items;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Item {
        private Long idProducto;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal proUnidad;
        private BigDecimal subtotal;
    }
}
