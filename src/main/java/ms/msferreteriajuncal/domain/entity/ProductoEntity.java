package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "productos")
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "nom_producto", length = 200, nullable = false)
    private String nombreProducto;

    @Column(name = "pro_categoria", length = 100)
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

    @Column(name = "pro_activo")
    private Boolean proActivo = true;
}
