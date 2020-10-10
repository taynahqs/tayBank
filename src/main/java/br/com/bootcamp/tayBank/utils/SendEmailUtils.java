package br.com.bootcamp.tayBank.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmailUtils {

    @Value("${email.from}")
    private static String de;

    @Value("${email.password}")
    private static String senha;

    public static void SendEmail(String para, String assunto, String mensagem) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(de,
                        senha);
            }
        });
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(de));

            Address[] toUser = InternetAddress.parse(para);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(assunto);
            message.setText(mensagem);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
