package ms.msferreteriajuncal.application.dto.out;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaResumenDto {
    private LocalDate desde;
    private LocalDate hasta;
    private long cantidadVentas;
    private long cantidadItems;
    private BigDecimal total;

    // Alias para PDF
    public long getItemsVendidos() {
        return this.cantidadItems;  // ajusta si tu campo se llama distinto (p.e. "items")
    }
    public java.math.BigDecimal getTotalVendido() {
        return this.total;  // ajusta si tu campo se llama "total"
    }


    public VentaResumenDto(LocalDate desde, LocalDate hasta, long cantidadVentas, long cantidadItems, BigDecimal total) {
        this.desde = desde;
        this.hasta = hasta;
        this.cantidadVentas = cantidadVentas;
        this.cantidadItems = cantidadItems;
        this.total = total;
    }
    public LocalDate getDesde() { return desde; }
    public LocalDate getHasta() { return hasta; }
    public long getCantidadVentas() { return cantidadVentas; }
    public long getCantidadItems() { return cantidadItems; }
    public BigDecimal getTotal() { return total; }
}
