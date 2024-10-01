package com.gustavolyra.spy_price_finder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${spring.sendgrid.api-key}")
    private String apiKey;


    public void sendEmail() {
        //TODO
    }


}
