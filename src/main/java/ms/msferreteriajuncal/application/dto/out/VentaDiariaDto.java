package ms.msferreteriajuncal.application.dto.out;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaDiariaDto {
    private LocalDate fecha;
    private BigDecimal total;

    public VentaDiariaDto(LocalDate fecha, BigDecimal total) {
        this.fecha = fecha;
        this.total = total;
    }
    public LocalDate getFecha() { return fecha; }
    public BigDecimal getTotal() { return total; }
}
