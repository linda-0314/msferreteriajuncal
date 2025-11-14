package ms.msferreteriajuncal.application.dto.in;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoDto {

    private Long idProducto;              // ✔ correcto
    private String nombreProducto;
    private String proCategoria;
    private int proUnidad;
    private int proCantidad;
    private long proPrecioEntrada;
    private long proPrecioSalida;
    private long proDescuento;
}
