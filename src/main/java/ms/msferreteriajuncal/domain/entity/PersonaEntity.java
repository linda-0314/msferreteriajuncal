package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity // esto es una entidad
@Table(name = "persona") // se Crea una tabla en la base de datos llamada persona
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // le decimos q va a ser tipo autoincrementable

    @Column (name = "id_Persona")
    private long idPersona;

    @Column (length = 60, name = "Per_Nombre")
    private String perNombre;

    @Column  (length = 60,name = "Per_Apellido")
    private String perApellido;

    @Column  (length = 50,name = "Per_TipoDocumento ")
    private String perTipoDocumento;

    @Column  (length = 50,name = "Per_Identidad")
    private String perIdentidad;

    @Column  (length = 60,name = "Per_Direccion")
    private String PerDireccion;

}
