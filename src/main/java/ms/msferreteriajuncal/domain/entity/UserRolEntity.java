package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios_Roles")
public class UserRolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // ⚙️ Campo antiguo (se mantiene para compatibilidad con tu servicio)
    private Long id_Rol;

    // 🔗 Nueva relación con la tabla Roles (opcional para usar objetos)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Rol", insertable = false, updatable = false)
    private RolesEntity rolesEntity;

    // 🔗 Relación con Usuario
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_Usuario")
    private UserEntity userEntity;
}
