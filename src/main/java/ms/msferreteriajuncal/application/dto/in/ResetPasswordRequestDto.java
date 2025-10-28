package ms.msferreteriajuncal.application.dto.in;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPasswordRequestDto {
    private String token;
    private String newPassword;
}
