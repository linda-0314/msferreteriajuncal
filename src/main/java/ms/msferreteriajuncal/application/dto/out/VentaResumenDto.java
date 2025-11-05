package ms.msferreteriajuncal.application.dto.out;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class VentaResumenDto {
    private LocalDate desde;
    private LocalDate hasta;

    // Lo que muestra la tabla del PDF:
    private int cantidadVentas;     // "Cantidad de ventas"
    private long itemsVendidos;     // "Items vendidos"
    private BigDecimal totalVendido; // "Total vendido"
}
