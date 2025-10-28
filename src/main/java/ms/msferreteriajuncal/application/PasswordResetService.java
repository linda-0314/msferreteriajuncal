package ms.msferreteriajuncal.application;

import ms.msferreteriajuncal.application.port.interactor.IPasswordResetService;
import ms.msferreteriajuncal.domain.entity.PasswordResetTokenEntity;
import ms.msferreteriajuncal.domain.entity.UserEntity;
import ms.msferreteriajuncal.infrastructure.repository.IPasswordResetTokenRepository;
import ms.msferreteriajuncal.infrastructure.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService implements IPasswordResetService {

 private final IUsuarioRepository userRepo;
 private final IPasswordResetTokenRepository tokenRepo;
 private final JavaMailSender mailSender;

 @Value("${app.frontend.reset-url}")
 private String resetBaseUrl;

 @Value("${app.mail.from:${spring.mail.username}}")
 private String from;

 // 2 horas de validez
 private static final int TOKEN_MINUTES = 120;

 public PasswordResetService(IUsuarioRepository userRepo,
                             IPasswordResetTokenRepository tokenRepo,
                             JavaMailSender mailSender) {
  this.userRepo = userRepo;
  this.tokenRepo = tokenRepo;
  this.mailSender = mailSender;
 }

 @Override
 @Transactional
 public String requestPasswordReset(String usernameOrEmail) {
  // 1) Buscar usuario por username o email
  UserEntity user = userRepo
          .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
          .orElse(null);

  // Si no existe, NO guardes token (tu entidad exige user!=null) y devuelve uno dummy (para dev)
  if (user == null) {
   return UUID.randomUUID().toString();
  }

  // 2) Generar y guardar token
  String token = UUID.randomUUID().toString();
  PasswordResetTokenEntity e = new PasswordResetTokenEntity();
  e.setToken(token);
  e.setUser(user);
  e.setCreatedAt(LocalDateTime.now());
  e.setExpiresAt(LocalDateTime.now().plusMinutes(TOKEN_MINUTES));
  e.setUsed(false);
  tokenRepo.save(e);

  // 3) Enviar correo
  if (user.getEmail() != null && !user.getEmail().isBlank()) {
   String link = resetBaseUrl + token;

   SimpleMailMessage msg = new SimpleMailMessage();
   msg.setFrom(from);                 // Debe coincidir con spring.mail.username
   msg.setTo(user.getEmail());        // destinatario
   msg.setSubject("Restablecer contraseña");
   msg.setText("""
                    Hola,

                    Para restablecer tu contraseña, abre este enlace:
                    %s

                    Si no fuiste tú, ignora este correo.
                    """.formatted(link));

   mailSender.send(msg);
  }

  return token; // útil en desarrollo
 }

 @Override
 @Transactional
 public void resetPassword(String token, String newPassword) {
  var tr = tokenRepo.findByToken(token)
          .orElseThrow(() -> new RuntimeException("Token inválido"));

  if (Boolean.TRUE.equals(tr.getUsed()))
   throw new RuntimeException("Token ya usado");

  if (tr.getExpiresAt().isBefore(LocalDateTime.now()))
   throw new RuntimeException("Token expirado");

  var user = tr.getUser();
  if (user == null) throw new RuntimeException("Usuario no encontrado");

  var encoder = new BCryptPasswordEncoder();
  user.setPassword(encoder.encode(newPassword));
  userRepo.save(user);

  tr.setUsed(true);
  tokenRepo.save(tr);

  // Limpia otros tokens del mismo usuario (opcional)
  try { tokenRepo.deleteByUser(user); } catch (Exception ignored) {}
 }
}
