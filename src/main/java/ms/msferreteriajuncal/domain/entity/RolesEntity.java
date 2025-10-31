package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Roles")
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "rol_name", length = 60, nullable = false, unique = true)
    private String rol_Name;

    // IMPORTANTE: el mappedBy DEBE COINCIDIR con el nombre del campo en UserRolEntity
    @OneToMany(mappedBy = "rolesEntity", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<UserRolEntity> userRoles = new HashSet<>();
}
