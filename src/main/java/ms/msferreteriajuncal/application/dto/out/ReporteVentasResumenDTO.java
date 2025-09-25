package ms.msferreteriajuncal.application.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReporteVentasResumenDTO {
    private LocalDate desde;
    private LocalDate hasta;
    private BigDecimal totalPeriodo;
    private List<VentaDiariaDTO> diarias;
    private List<VentaPorProductoDTO> porProducto;
    private List<VentaPorUsuarioDTO> porUsuario;
}