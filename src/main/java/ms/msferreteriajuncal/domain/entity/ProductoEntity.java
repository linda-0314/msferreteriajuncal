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

    @Column(name = "Pro_Categoria", length = 60)
    private String proCategoria;

    @Column(name = "Pro_unidad")
    private int proUnidad;

    @Column(name = "Pro_cantidad")
    private int proCantidad;

    @Column(name = "Pro_PrecioEntrada")
    private long proPrecioEntrada;

    @Column(name = "Pro_PrecioSalida")
    private long proPrecioSalida;

    @Column(name = "Pro_Descuento")
    private long proDescuento;
}