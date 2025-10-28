package ms.msferreteriajuncal.application.dto.in;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForgotPasswordRequestDto {
    private String usernameOrEmail;
}
