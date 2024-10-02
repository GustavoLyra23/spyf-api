package com.gustavolyra.spy_price_finder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavolyra.spy_price_finder.config.MercadoLibreConfig;
import com.gustavolyra.spy_price_finder.dto.product.ProductDto;
import com.gustavolyra.spy_price_finder.service.util.JSONUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        ResponseEntity<String> responseEntity = mercadoLibreConfig.getSearchResultsPaginated(productName, 0, 20);
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        JsonNode resultsNode = jsonNode.get("results");

        JSONUtil.isNodeValid(resultsNode);

        List<JsonNode> filteredProducts = JSONUtil.filterProducts(resultsNode, productMinPrice);
        JsonNode bestProduct = JSONUtil.findBestProduct(filteredProducts);

        double minPrice = bestProduct.get("price").asDouble();
        String minPriceLink = bestProduct.get("permalink").asText();
        String minPriceTitle = bestProduct.get("title").asText();

        return new ProductDto(minPrice, minPriceLink, minPriceTitle);
    }
}
