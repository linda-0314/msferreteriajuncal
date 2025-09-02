package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( name ="DetallesVenta")
public class DetallesVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDetallesVentas;

    @ManyToOne
    @JoinColumn(name = "id_Venta")
    private VentasEntity idVenta;

    @ManyToOne
    @JoinColumn(name = "id_Producto")
    private ProductoEntity idProducto;

    @Column  (length = 50,name = "cantidad")
    private int cantidad;

    @Column  (length = 50,name = "precio")
    private int precio;

}


