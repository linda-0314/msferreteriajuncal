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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VentaCreateDTO {
    private Long userId;

    @NotEmpty
    private List<Item> items; // lista de producto venddidos


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item  {

        @NotNull
        private Long idProducto;
        @NotNull @Min(1)
        private Integer cantidad;// UND
        private BigDecimal precioUnitario;
    }
}
