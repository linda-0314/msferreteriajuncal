package ms.msferreteriajuncal.application.dto.out;

import java.math.BigDecimal;

public class InventarioValorDto {
    private long cantidadProductos;
    private BigDecimal valorTotal;

    public InventarioValorDto(long cantidadProductos, BigDecimal valorTotal) {
        this.cantidadProductos = cantidadProductos;
        this.valorTotal = valorTotal;
    }
    public long getCantidadProductos() { return cantidadProductos; }
    public BigDecimal getValorTotal() { return valorTotal; }
}
