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

    private long id_Rol;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_Usuario")
    private UserEntity userEntity ;
}
