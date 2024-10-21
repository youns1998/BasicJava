package SERVICE;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; 

public class EmailService {
    private static final String FROM_EMAIL = "youns1998@gmail.com";
    private static final String PASSWORD = "코드 보안";
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    public void sendVerificationEmail(String toEmail, String code) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("이메일 인증 코드");
            message.setText("회원가입을 위해 다음 인증 코드를 입력하세요: " + code);
            Transport.send(message);
            System.out.println("인증 이메일이 성공적으로 발송되었습니다.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}