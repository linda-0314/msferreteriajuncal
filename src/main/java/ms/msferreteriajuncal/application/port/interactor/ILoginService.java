package ms.msferreteriajuncal.application.port.interactor;

import ms.msferreteriajuncal.application.dto.in.LoginRequestDto;
import ms.msferreteriajuncal.domain.entity.UserEntity;

public interface ILoginService {
       void login(LoginRequestDto loginRequestDto);
}
