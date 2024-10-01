package com.gustavolyra.spy_price_finder.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavolyra.spy_price_finder.dto.ProductDto;
import com.gustavolyra.spy_price_finder.service.util.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

@Service
public class AmazonScrappingService {

    @Value("${scrap.api.key}")
    private String apiKey;
    private final ObjectMapper objectMapper;

    public AmazonScrappingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ProductDto scrapAmazonProduct(String productName, double productMinPrice) {
        try {
            String country = "br";
            String url = "https://api.scraperapi.com/structured/amazon/search?api_key=" + apiKey + "&query=" + productName + "&country=" + country;
            URL urlForGetRequest = new URL(url);
            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");
            int responseCode = conection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();

                JsonNode jsonNode = objectMapper.readTree(response.toString());
                JsonNode resultsNode = jsonNode.get("results");

                JSONUtil.isNodeValid(resultsNode);
                List<JsonNode> filteredProducts = JSONUtil.filterProducts(resultsNode, productMinPrice);
                JsonNode bestProduct = filteredProducts.stream()
                        .min(Comparator.comparingDouble(product -> product.get("price").asDouble()))
                        .orElseThrow(() -> new RuntimeException("No products found with a price >= " + productMinPrice));

                double minPrice = bestProduct.get("price").asDouble();
                String minPriceLink = bestProduct.get("url").asText();
                String minPriceTitle = bestProduct.get("name").asText();

                return new ProductDto(minPrice, minPriceLink, minPriceTitle);

            } else {
                System.out.println("GET NOT WORKED");
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
            return null;
        }
    }
}
