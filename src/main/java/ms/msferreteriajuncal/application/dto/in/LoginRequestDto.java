package ms.msferreteriajuncal.application.dto.in;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    private String usernameOrEmail;
    private String password;
}
