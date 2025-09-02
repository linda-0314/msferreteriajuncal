package ms.msferreteriajuncal.application;

import ms.msferreteriajuncal.application.dto.in.ProveedorDto;
import ms.msferreteriajuncal.application.port.interactor.IProveeService;
import ms.msferreteriajuncal.domain.entity.ProveedorEntity;
import ms.msferreteriajuncal.infrastructure.repository.IProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService implements IProveeService{

    @Autowired
    private IProveedorRepository proveedorRepository;// SE INYECTA

    @Override
    public List<ProveedorEntity> listarProvee(){ //LISTAR
         return proveedorRepository.findAll();
    }

    @Override
    public Optional<ProveedorEntity> getProveedorById(Long id) {  // POR ID
        return proveedorRepository.findById(id);
    }


    @Override
    public ProveedorDto guardarpro(ProveedorDto proveedor) {
        ProveedorEntity proveedorEntity;

        if (proveedor.getIdProveedor() != 0 && proveedorRepository.existsById(proveedor.getIdProveedor())) {
            // Si existe, se puede modificar
            proveedorEntity = proveedorRepository.findById(proveedor.getIdProveedor()).get();
        } else {
            // Si no existe, lo creamos
            proveedorEntity = new ProveedorEntity();
        }

        proveedorEntity.setNomProveedor(proveedor.getNomProveedor());
        proveedorEntity.setTelefono(proveedor.getTelefono());
        proveedorEntity.setCorreo(proveedor.getCorreo());
        proveedorEntity.setValorCompra(proveedor.getValorCompra());
        proveedorEntity.setDireccionProveedor(proveedor.getDireccionProveedor());

        ProveedorEntity savedProveedor = proveedorRepository.save(proveedorEntity);
        proveedor.setIdProveedor(savedProveedor.getIdProveedor()); // Devuelvo el id generado
        return proveedor;
    }

    @Override
    public void eliminarProveedorPorId(Long idProveedor) {
        proveedorRepository.deleteById(idProveedor);
    }
}
