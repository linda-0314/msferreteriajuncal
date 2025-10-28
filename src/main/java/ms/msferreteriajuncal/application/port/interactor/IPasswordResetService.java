package ms.msferreteriajuncal.application.port.interactor;

public interface IPasswordResetService {
    String requestPasswordReset(String usernameOrEmail);
    void resetPassword(String token, String newPassword);
}
