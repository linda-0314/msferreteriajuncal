package ms.msferreteriajuncal.application;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void enviarConAdjunto(String to, String subject, String body, byte[] pdf, String fileName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            helper.addAttachment(fileName, new ByteArrayResource(pdf));
            mailSender.send(message);
        } catch (Exception e) {
            // log.warn("No se pudo enviar correo: " + e.getMessage());
        }
    }
}
