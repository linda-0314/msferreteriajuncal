package ms.msferreteriajuncal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Proveedor")
public class ProveedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "Id_Proveedor")
    private long idProveedor;

    @Column (name = "Nom_Proveedor")
    private String nomProveedor;

    @Column (length = 60, name = "Telefono")
    private String  telefono;

    @Column  (length = 60,name = "Correo")
    private String  correo;

    @Column  (length = 50,name = "Valor_Compra ")
    private String  valorCompra;

    @Column  (length = 50,name = "Direccion_Proveedor ")
    private String   direccionProveedor;



}
