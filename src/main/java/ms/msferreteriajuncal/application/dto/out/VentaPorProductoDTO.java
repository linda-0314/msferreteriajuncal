package ms.msferreteriajuncal.application.dto.out;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class VentaPorProductoDTO {
    private Long idProducto;
    private String nombreProducto;
    private Long cantidadTotal;
    private BigDecimal totalVendido;

}