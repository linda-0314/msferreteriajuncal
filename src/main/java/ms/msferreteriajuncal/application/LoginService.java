package ms.msferreteriajuncal.application;

import ms.msferreteriajuncal.application.dto.in.LoginRequestDto;
import ms.msferreteriajuncal.application.port.interactor.ILoginService;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import ms.msferreteriajuncal.infrastructure.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class LoginService implements ILoginService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    /**
     * Valida credenciales y, si el usuario aún tiene password en texto plano,
     * lo migra automáticamente a BCrypt (upgrade transparente).
     * No cambia el contrato: sigue siendo void; si algo falla, lanza RuntimeException.
     */
    @Override
    @Transactional
    public void login(LoginRequestDto loginRequestDto) {

        // ⚠️ ADAPTA ESTA LÍNEA a tu DTO si no se llama así:
        // Si tu DTO tiene getUsername() en vez de getUsernameOrEmail(), usa ese.
        String userOrEmail = loginRequestDto.getUsernameOrEmail();
        String raw = loginRequestDto.getPassword();

        UserEntity user = iUsuarioRepository
                .findByUsernameOrEmail(userOrEmail, userOrEmail)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        String stored = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        boolean isBCrypt = stored != null && (
                stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")
        );

        if (isBCrypt) {
            // Usuario ya migrado: comparamos hash vs texto ingresado
            if (!encoder.matches(raw, stored)) {
                throw new RuntimeException("Credenciales inválidas");
            }
        } else {
            // Usuario antiguo (password en texto): compatibilidad + upgrade a BCrypt
            if (!stored.equals(raw)) {
                throw new RuntimeException("Credenciales inválidas");
            }
            user.setPassword(encoder.encode(raw)); // upgrade en caliente
            iUsuarioRepository.save(user);         // se persiste el nuevo hash
        }

        // Si llegaste aquí, las credenciales son correctas.
        // Como el contrato es void, no devolvemos nada: tu controlador debe responder 200 OK.
        // (Tu lógica adicional de sesión/JWT permanece como la tengas implementada fuera de aquí.)
    }
}
