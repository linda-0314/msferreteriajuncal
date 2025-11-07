package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ventas")
public class VentasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVentas;

    @Column(length = 50, name = "Fecha")
    private LocalDateTime fecha;

    @Column(name = "total")
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_Usuario")
    private UserEntity user;

    @Column(name = "cliente_nombre", length = 120)
    private String clienteNombre;

    @Column(name = "cliente_documento", length = 60)
    private String clienteDocumento;

    @Column(name = "cliente_email", length = 150)
    private String clienteEmail;

    @OneToMany(mappedBy = "idVenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallesVenta> detalles;
}
