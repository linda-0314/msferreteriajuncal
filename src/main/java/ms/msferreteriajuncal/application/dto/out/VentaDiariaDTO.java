package ms.msferreteriajuncal.application.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class VentaDiariaDTO {
    private LocalDate dia;
    private BigDecimal total;

    public VentaDiariaDTO(java.sql.Date dia, BigDecimal total) {
        this.dia = dia.toLocalDate();
        this.total = total;
    }
}