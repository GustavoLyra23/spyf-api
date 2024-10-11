package com.gustavolyra.spy_price_finder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavolyra.spy_price_finder.dto.product.ProductDto;
import com.gustavolyra.spy_price_finder.service.exceptions.HttpConectionException;
import com.gustavolyra.spy_price_finder.service.util.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class AmazonService {

    @Value("${scrap.api.key}")
    private String apiKey;
    private final ObjectMapper objectMapper;

    public AmazonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<ProductDto> scrapAmazonProduct(String productName, double productMinPrice) throws JsonProcessingException {

        var response = getHttpResponse(productName);

            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode resultsNode = jsonNode.get("results");

            JSONUtil.isNodeValid(resultsNode);
            List<JsonNode> filteredProducts = JSONUtil.filterProducts(resultsNode, productMinPrice);
        var jsonNodes = JSONUtil.findBestProduct(filteredProducts);
        if (jsonNodes.isEmpty()) {
            return null;
        }

        return jsonNodes.stream()
                .map(product -> new ProductDto(product.get("price").asDouble(),
                        product.get("url").asText(), product.get("name").asText()))
                .toList();
    }


    private String getHttpResponse(String productName) {
        try {
            String country = "br";
            String tld = "com.br";
            String url = "https://api.scraperapi.com/structured/amazon/search?api_key=" + apiKey
                    + "&query=" + productName + "&country=" + country + "&tld=" + tld;
            URL urlForGetRequest = new URL(url);
            String readLine;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");
            int responseCode = conection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
            }
            return response.toString();
        } catch (IOException e) {
            throw new HttpConectionException(e.getMessage());
        }
    }
}
