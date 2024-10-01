package com.gustavolyra.spy_price_finder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gustavolyra.spy_price_finder.dto.ProductDto;
import com.gustavolyra.spy_price_finder.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/find-product")
    public ResponseEntity<ProductDto> findBestProduct(@RequestParam String productName,
                                                      @RequestParam(defaultValue = "0.0") Double productMinPrice) throws JsonProcessingException {
        var bestProduct = productService.findBestProduct(productName, productMinPrice);
        return ResponseEntity.ok(bestProduct);
    }
}
