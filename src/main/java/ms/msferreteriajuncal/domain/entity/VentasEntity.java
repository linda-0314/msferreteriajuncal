package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ventas")
public class VentasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVentas;

    @Column  (length = 50,name = "Fecha")
    private LocalDateTime fecha;

    @Column (name = "total")
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_Usuario")
    private UserEntity user;// hizo la venta

}
