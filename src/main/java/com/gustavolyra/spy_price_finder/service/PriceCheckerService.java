package com.gustavolyra.spy_price_finder.service;

import com.gustavolyra.spy_price_finder.repository.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class PriceCheckerService {

    private final ProductRepository productRepository;
    private final ScrapService scrapService;
    private final MailService mailService;

    public PriceCheckerService(ProductRepository productRepository, ScrapService scrapService, MailService mailService) {
        this.productRepository = productRepository;
        this.scrapService = scrapService;
        this.mailService = mailService;
    }

    @Transactional
    @Scheduled(cron = "0 0 */1 * * *")
    public void checkPriceAndNotify() {
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
