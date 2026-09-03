package vn.edu.hcmute.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailService {
    private final Properties settings = loadSettings();

    public void sendOtp(String recipient, String otp, String purpose) {
        String username = required("mail.username");
        String password = required("mail.password");
        Properties smtp = new Properties();
        smtp.put("mail.smtp.host", settings.getProperty("mail.host", "smtp.gmail.com"));
        smtp.put("mail.smtp.port", settings.getProperty("mail.port", "587"));
        smtp.put("mail.smtp.auth", "true");
        smtp.put("mail.smtp.starttls.enable", settings.getProperty("mail.starttls", "true"));

        Session session = Session.getInstance(smtp, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(settings.getProperty("mail.from", username), "Bài tập Web"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("ACTIVATE".equals(purpose) ? "Mã kích hoạt tài khoản" : "Mã đặt lại mật khẩu");
            message.setText("Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 10 phút. Không chia sẻ mã này.");
            Transport.send(message);
        } catch (Exception exception) {
            throw new MailDeliveryException("Không thể gửi OTP. Hãy kiểm tra cấu hình SMTP.", exception);
        }
    }

    private String required(String key) {
        String value = settings.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new MailDeliveryException("Chưa cấu hình " + key + " trong ~/.baitap-mail.properties", null);
        }
        return value;
    }

    private Properties loadSettings() {
        Properties properties = new Properties();
        Path path = Path.of(System.getProperty("user.home"), ".baitap-mail.properties");
        if (!Files.isRegularFile(path)) return properties;
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Không thể đọc cấu hình email", exception);
        }
    }
}
