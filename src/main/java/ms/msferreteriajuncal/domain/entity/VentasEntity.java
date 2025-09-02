package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ventas")
public class VentasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVentas;

    @Column  (length = 50,name = "Fecha")
    private String fecha;

    @ManyToOne
    @JoinColumn(name = "id_Usuario")
    private UserEntity user;

}
