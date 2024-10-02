package com.gustavolyra.spy_price_finder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gustavolyra.spy_price_finder.dto.product.ProductDto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    private final MercadoLibreService mercadoLibreService;
    private final AmazonScrappingService amazonScrappingService;


    public ProductService(MercadoLibreService mercadoLibreService, AmazonScrappingService amazonScrappingService) {
        this.mercadoLibreService = mercadoLibreService;
        this.amazonScrappingService = amazonScrappingService;
    }

    public ProductDto findBestProduct(String productName, Double productMinPrice) throws JsonProcessingException {
        ProductDto mercadoLibreProduct = mercadoLibreService.findProduct(productName, productMinPrice);
        ProductDto amazonProduct = amazonScrappingService.scrapAmazonProduct(productName, productMinPrice);

        if (mercadoLibreProduct == null) {
            return amazonProduct;
        } else if (amazonProduct == null) {
            return mercadoLibreProduct;
        }

        return mercadoLibreProduct.price() < amazonProduct.price() ? mercadoLibreProduct : amazonProduct;
    }

    public void watchOffer(String url) {
        if (!(!url.contains("mercadolivre") || !url.contains("amazon"))) {
            throw new IllegalArgumentException("Invalid URL");
        }
        System.setProperty("webdriver.edge.driver", "C:\\Users\\gustavo\\Downloads\\edgedriver_win64 (1)\\msedgedriver.exe");
        WebDriver driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try {
            driver.get(url);
            WebElement productTitleElement = findProductTitle(driver, url);

            if (productTitleElement != null) {
                String productTitle = productTitleElement.getText();
                System.out.println("Título do Produto: " + productTitle);
                WebElement priceElement = findProductPrice(driver, url);

                if (priceElement != null) {
                    String price = priceElement.getText();
                    System.out.println("Preço do Produto: " + price);
                } else {
                    System.out.println("Preço não encontrado");
                }
            } else {
                System.out.println("Produto não encontrado");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private WebElement findProductTitle(WebDriver driver, String url) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        if (url.contains("mercadolivre")) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.ui-pdp-title")));
        } else if (url.contains("amazon")) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
        }
        return null;
    }

    private WebElement findProductPrice(WebDriver driver, String url) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        if (url.contains("mercadolivre")) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".andes-money-amount__fraction")));
        } else if (url.contains("amazon")) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("priceblock_ourprice")));
        }
        return null;
    }


}

