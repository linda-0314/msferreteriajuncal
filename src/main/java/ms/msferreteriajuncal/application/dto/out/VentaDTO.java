package ms.msferreteriajuncal.application.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {
    private long idVenta;
    private LocalDateTime fecha;
    private long userId;
    private BigDecimal total;
    private List<Item> items;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor

    public static class Item {
        private Long idProducto;          //
        private String nombreProducto;    //
        private Integer cantidad;         // uni vendidas
        private BigDecimal proUnidad;// precio por unidad
        private BigDecimal subtotal;      // cantidad * precioUnitario
    }




}
