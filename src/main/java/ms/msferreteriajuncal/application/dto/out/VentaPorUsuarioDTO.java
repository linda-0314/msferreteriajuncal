package ms.msferreteriajuncal.application.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class VentaPorUsuarioDTO {
    private Long idUsuario;
    private String username;
    private Long cantidadVentas;   // número de ventas
    private BigDecimal totalVentas;
}