package com.gustavolyra.spy_price_finder.service.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.gustavolyra.spy_price_finder.service.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JSONUtil {

    private JSONUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void isNodeValid(JsonNode resultsNode) {
        if (resultsNode == null || !resultsNode.isArray() || resultsNode.isEmpty()) {
            throw new ResourceNotFoundException("No results found");
        }
    }

    public static List<JsonNode> filterProducts(JsonNode resultsNode, double productMinPrice) {
        List<JsonNode> filteredProducts = new ArrayList<>();
        resultsNode.forEach(product -> {
            if (product.get("price") != null && product.get("price").asDouble() >= productMinPrice) {
                filteredProducts.add(product);
            }
        });
        return filteredProducts;
    }

    public static JsonNode findBestProduct(List<JsonNode> filteredProducts) {
        return filteredProducts.stream()
                .min(Comparator.comparingDouble(product -> product.get("price").asDouble()))
                .orElse(null);
    }


}
