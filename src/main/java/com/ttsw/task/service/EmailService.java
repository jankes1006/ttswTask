package com.ttsw.task.service;

import com.ttsw.task.domain.Mail;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
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

    public void sendMail(Mail mail) {
        SimpleMailMessage mailMessage = prepareMail(mail);
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage prepareMail(Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getReceiver());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getText());

        return mailMessage;
    }

    public void sendEmailToVerify(AppUser appUser, Token token) {
        Mail mail = new Mail(appUser.getEmail(), "Potwierdz adres email",
                "Aby potwierdzic konto, wejdz na adres: " + prepareTokenUrl(token));
        sendMail(mail);
    }

    private String prepareTokenUrl(Token token) {
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/user/verifyAccount")
                .queryParam("tokenValue", token.getValue()).build().toString();
        return url;
    }

    public void sendEmailThatVerifyIsCorrect(AppUser appUser) {
        Mail mail = new Mail(appUser.getEmail(), "Witaj w serwisie.",
                "Cześć " + appUser.getUsername() + "\nW poprawny sposób zweryfikowano konto.");
        sendMail(mail);
    }

    public void sendEmailToClientBuy(AppUser client, AppUser seller, Offer offer) {
        Mail mail = new Mail(client.getEmail(), "Zarezerwowałeś: " + offer.getTitle(),
                "Cześć " + client.getUsername() +
                        "\n Udało Ci się zarezerwować: \"" + offer.getTitle() + "\"." +
                        "\n W celu uzgodnienia warunkow sprzedazy skontaktuj sie z sprzedajacym, pod adresem: " +
                        seller.getEmail());
        sendMail(mail);
    }

    public void sendEmailToSellerBuy(AppUser client, AppUser seller, Offer offer) {
        Mail mail = new Mail(seller.getEmail(), "Zarezerwowano: " + offer.getTitle(),
                "Cześć " + seller.getUsername() +
                        "\n Zarezerwowano Twoją oferte o nazwie: \"" + offer.getTitle() + "\"." +
                        "\n W celu uzgodnienia warunkow sprzedazy skontaktuj sie z kupujacym, pod adresem: " +
                        client.getEmail());
        sendMail(mail);
    }

    public void sendEmailToSellerThatOfferIsBlocked(AppUser admin, AppUser seller, Offer offer, String reason) {
        Mail mail = new Mail(seller.getEmail(), "Zablokowano oferte: " + offer.getTitle(),
                "Cześć " + seller.getUsername() +
                        "\n Zablokowano twoją ofertę o nazwie: \"" + offer.getTitle() + "\"." +
                        "\n Powodem było \"" + reason + "\"" +
                        "\n Jeżeli nie zgadzasz się z powodem, skontaktuj się z: " + admin.getEmail());
        sendMail(mail);
    }

    public void sendEmailToSellerThatOfferIsUnBlock(AppUser seller, Offer offer) {
        Mail mail = new Mail(seller.getEmail(), "Odblokowano oferte: " + offer.getTitle(),
                "Cześć " + seller.getUsername() +
                        "\n Właśnie odblokowano Twoją ofertę o nazwie \"" + offer.getTitle() + "\"");
        sendMail(mail);
    }

    public void sendEmailResetPassword(AppUser appUser, Token token) {
        Mail mail = new Mail(appUser.getEmail(), "Zmiana hasła",
                "Cześć " + appUser.getUsername() + "!" +
                        "\n Aby zresetować hasło przejdź pod następujący adres: http://localhost:4200/setPassword/" + token.getValue());
        sendMail(mail);
    }
}
