package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Productos")
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "Id_Producto")
    private long idProducto;

    @Column(name = "Nom_Producto")
    private String nombreProducto;

    @Column(length = 60, name = "Pro_Categoria")
    private String proCategoria;

    @Column(length = 60, name = "Pro_unidad")
    private int proUnidad;

    @Column(length = 50, name = "Pro_cantidad ")
    private int proCantidad;

    @Column(length = 50, name = "Pro_PrecioEntrada ")
    private long proPrecioEntrada;

    @Column(length = 50, name = "Pro_PrecioSalida ")
    private long proPrecioSalida;

    @Column(length = 50, name = "Pro_Descuento ")
    private long proDescuento;


}
