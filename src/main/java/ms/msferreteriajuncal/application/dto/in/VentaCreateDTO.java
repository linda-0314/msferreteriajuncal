package ms.msferreteriajuncal.application.dto.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VentaCreateDTO {

    @NotNull
    private Long userId;

    private String clienteNombre;
    private String clienteDocumento;
    private String clienteEmail;

    @NotEmpty
    private List<Item> items;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Item {
        @NotNull
        private Long idProducto;
        @NotNull @Min(1)
        private Integer cantidad;
        private BigDecimal precioUnitario;
    }
}
