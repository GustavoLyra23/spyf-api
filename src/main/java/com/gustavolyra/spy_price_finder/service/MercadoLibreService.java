package com.gustavolyra.spy_price_finder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavolyra.spy_price_finder.config.MercadoLibreConfig;
import com.gustavolyra.spy_price_finder.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MercadoLibreService {

    private final MercadoLibreConfig mercadoLibreConfig;
    private final ObjectMapper objectMapper;

    public MercadoLibreService(MercadoLibreConfig mercadoLibreConfig, ObjectMapper objectMapper) {
        this.mercadoLibreConfig = mercadoLibreConfig;
        this.objectMapper = objectMapper;
    }

    public ProductDto findProduct(String productName, Double productMinPrice) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = mercadoLibreConfig.getSearchResultsPaginated(productName, 0, 50);
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        JsonNode resultsNode = jsonNode.get("results");

        if (resultsNode == null || !resultsNode.isArray() || resultsNode.isEmpty()) {
            return null;
        }

        List<JsonNode> filteredProducts = new ArrayList<>();
        resultsNode.forEach(product -> {
            if (product.get("price").asDouble() >= productMinPrice) {
                filteredProducts.add(product);
            }
        });

        JsonNode bestProduct = filteredProducts.stream()
                .min(Comparator.comparingDouble(product -> product.get("price").asDouble()))
                .orElseThrow(() -> new RuntimeException("No products found with a price >= " + productMinPrice));

        double minPrice = bestProduct.get("price").asDouble();
        String minPriceLink = bestProduct.get("permalink").asText();
        String minPriceTitle = bestProduct.get("title").asText();

        return new ProductDto(minPrice, minPriceLink, minPriceTitle);
    }
}
