package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( name ="Produ_Prove")
public class ProduProveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_Produ_Prove;

    @ManyToOne
    @JoinColumn(name = "id_provedor")
    private ProveedorEntity idProvedor;

    @ManyToOne
    @JoinColumn(name = "id_produ")
    private ProductoEntity idProdu;
}
