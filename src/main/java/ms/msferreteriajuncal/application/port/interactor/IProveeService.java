package ms.msferreteriajuncal.application.port.interactor;

import ms.msferreteriajuncal.application.dto.in.ProductoDto;
import ms.msferreteriajuncal.application.dto.in.ProveedorDto;
import ms.msferreteriajuncal.domain.entity.ProductoEntity;
import ms.msferreteriajuncal.domain.entity.ProveedorEntity;

import java.util.List;
import java.util.Optional;

public interface IProveeService {


    List<ProveedorEntity> listarProvee();// DEVUELVE TODOS LO PRODUCTOS

    Optional<ProveedorEntity> getProveedorById(Long id); // BUSCA POR SU ID

    ProveedorDto guardarpro(ProveedorDto ProveedorDto); // crear o actualizar RECIBE EN DTO


    void eliminarProveedorPorId(Long idProveedor); // ELIMINAR


}
