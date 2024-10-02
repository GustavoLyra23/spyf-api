package com.gustavolyra.spy_price_finder.service;

import com.gustavolyra.spy_price_finder.repository.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PriceCheckerService {


    private ProductRepository productRepository;
    private ScrapService scrapService;
    private MailService mailService;

    @Scheduled(cron = "0 0 */1 * * *")
    private void checkPriceAndNotify() {
        var products = productRepository.findAll();
        products.forEach(product -> {
            Double currentPrice = scrapService.scrapPageFromUrl(product.getLink()).price();
            if (currentPrice < product.getPrice()) {
                product.setPrice(currentPrice);
                productRepository.save(product);
                product.getUsers().forEach(user -> {
                    try {
                        mailService.sendEmail(user.getEmail(), "The product " + product.getTitle() + " has a new price: " + currentPrice);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }
}
