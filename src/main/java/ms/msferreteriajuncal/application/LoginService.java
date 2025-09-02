package ms.msferreteriajuncal.application;

import ms.msferreteriajuncal.application.dto.in.LoginRequestDto;
import ms.msferreteriajuncal.application.port.interactor.ILoginService;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import ms.msferreteriajuncal.infrastructure.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements ILoginService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;// se inyecta el repositorio a la entidad

    @Override
    public void login(LoginRequestDto loginRequestDto) {
        Optional<UserEntity> userEntityOptional = iUsuarioRepository.findByUsernameOrEmail(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getUsernameOrEmail());

        if (userEntityOptional.isEmpty()) {
            throw new RuntimeException("Usuario o correo no registrado.");// si no entra el mismo valor q pide el login
        }

        if(!userEntityOptional.get().getPassword().equals(loginRequestDto.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

    }

}