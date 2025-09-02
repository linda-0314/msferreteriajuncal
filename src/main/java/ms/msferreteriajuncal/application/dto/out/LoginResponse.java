package ms.msferreteriajuncal.application.dto.out;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    public LoginResponse(String token) {
        this.token = token;
    }
}