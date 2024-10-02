package com.gustavolyra.spy_price_finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpyPriceFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpyPriceFinderApplication.class, args);
    }

}
