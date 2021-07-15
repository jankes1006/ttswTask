package com.ttsw.task.service;

import com.ttsw.task.entity.AppUser;
import com.ttsw.task.domain.Mail;
import com.ttsw.task.entity.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(Mail mail){
        SimpleMailMessage mailMessage = prepareMail(mail);
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage prepareMail(Mail mail){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getReceiver());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getText());

        return mailMessage;
    }

    public void sendEmailToVerify(AppUser appUser, Token token){
        Mail mail = new Mail(appUser.getEmail(),"Potwierdz adres email",
                "Aby potwierdzic konto, wejdz na adres: "+prepareTokenUrl(token));
        sendMail(mail);
    }

    private String prepareTokenUrl(Token token){
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/user/verifyAccount")
                .queryParam("tokenValue",token.getValue()).build().toString();
        return url;
    }

    public void sendEmailThatVerifyIsCorrect(AppUser appUser){
        Mail mail = new Mail(appUser.getEmail(),"Witaj w serwisie.",
                "Cześć "+appUser.getUsername()+"\nW poprawny sposób zweryfikowano konto.");
        sendMail(mail);
    }
}
