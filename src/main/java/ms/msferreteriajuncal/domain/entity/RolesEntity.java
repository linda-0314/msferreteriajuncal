package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name = "Roles")// se Crea una tabla en la base de datos llamada Roles
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 60)
    private String rol_Name;
}
