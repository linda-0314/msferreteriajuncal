package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "productos") // <-- minúsculas
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")       // <-- minúsculas
    private Long idProducto;            // usa Long (wrapper)

    @Column(name = "nom_producto")
    private String nombreProducto;

    @Column(name = "pro_categoria", length = 60)
    private String proCategoria;

    @Column(name = "pro_unidad")
    private Integer proUnidad;

    @Column(name = "pro_cantidad")
    private Integer proCantidad;

    @Column(name = "pro_precio_entrada")
    private Long proPrecioEntrada;

    @Column(name = "pro_precio_salida")
    private Long proPrecioSalida;

    @Column(name = "pro_descuento")
    private Long proDescuento;
}
