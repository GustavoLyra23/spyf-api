package com.gustavolyra.spy_price_finder.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService {

    @Value("${spring.sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.from}")
    private String sender;


    public void sendEmail(String reciever, String message) throws IOException {
        Email from = new Email(sender);
        String subject = "Price Alert";
        Email to = new Email(reciever);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(System.getenv(apiKey));
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }


}
