package ms.msferreteriajuncal.application.dto.out;

import java.math.BigDecimal;

public class TopProductoDto {
    private Long idProducto;
    private String nombre;
    private long cantidadVendida;
    private BigDecimal totalVendido;

    // Alias para PDF: el util espera getNombreProducto()
    public String getNombreProducto() {
        // si tu campo se llama "nombre", devuelve ese
        return this.nombre;
    }


    public TopProductoDto(Long idProducto, String nombre, long cantidadVendida, BigDecimal totalVendido) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidadVendida = cantidadVendida;
        this.totalVendido = totalVendido;
    }
    public Long getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public long getCantidadVendida() { return cantidadVendida; }
    public BigDecimal getTotalVendido() { return totalVendido; }
}
