package com.gustavolyra.spy_price_finder.service;

import com.gustavolyra.spy_price_finder.dto.product.ProductDto;
import com.gustavolyra.spy_price_finder.service.exceptions.HttpConectionException;
import com.gustavolyra.spy_price_finder.service.exceptions.ResourceNotFoundException;
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
public class ScrapService {

    public ProductDto scrapPageFromUrl(String url) {
        if (!(!url.contains("mercadolivre") || !url.contains("amazon"))) {
            throw new IllegalArgumentException("Invalid URL");
        }
        System.setProperty("webdriver.edge.driver", "C:\\Users\\gustavo\\Downloads\\edgedriver_win64 (1)\\msedgedriver.exe");
        WebDriver driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(url);
            WebElement productTitleElement = findProductTitle(driver, url);
            String productTitle = productTitleElement.getText();
            WebElement priceElement = findProductPrice(driver, url);
            String price = priceElement.getText();
            if (productTitle != null && price != null) {
                return new ProductDto(Double.valueOf(price), url, productTitle);
            }
        } catch (Exception e) {
            throw new HttpConectionException("Error on Scraping page");
        } finally {
            driver.quit();
        }
        throw new ResourceNotFoundException("Product not found");
    }

    private WebElement findProductTitle(WebDriver driver, String url) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        if (url.contains("mercadolivre")) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.ui-pdp-title")));
        } else if (url.contains("amazon")) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
        }
        throw new ResourceNotFoundException("Title not found");
    }

    private WebElement findProductPrice(WebDriver driver, String url) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        if (url.contains("mercadolivre")) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".andes-money-amount__fraction")));
        } else if (url.contains("amazon")) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("priceblock_ourprice")));
        }
        throw new ResourceNotFoundException("Price not found");
    }

}
