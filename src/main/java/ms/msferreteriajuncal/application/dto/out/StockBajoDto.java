package ms.msferreteriajuncal.application.dto.out;

public class StockBajoDto {
    private Long idProducto;
    private String nombre;
    private int stock;

    public StockBajoDto(Long idProducto, String nombre, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.stock = stock;
    }
    // Alias para PDF
    public String getNombreProducto() {
        return this.nombre;
    }
    public int getCantidadActual() {
        return this.stock;
    }

    public Long getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
}
