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

    public List<ProductDto> findProduct(String productName, Double productMinPrice) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = mercadoLibreConfig.getSearchResultsPaginated(productName, 0, 20);
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        JsonNode resultsNode = jsonNode.get("results");

        JSONUtil.isNodeValid(resultsNode);

        List<JsonNode> filteredProducts = JSONUtil.filterProducts(resultsNode, productMinPrice);

        var jsonNodes = JSONUtil.findBestProduct(filteredProducts);
        if (jsonNodes.isEmpty()) {
            return null;
        }

        return jsonNodes.stream().map(product -> new ProductDto(product.get("price").asDouble(), product.get("permalink").asText(), product.get("title").asText())).toList();
    }
}
