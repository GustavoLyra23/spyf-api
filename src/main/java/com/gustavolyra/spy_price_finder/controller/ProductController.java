package com.gustavolyra.spy_price_finder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gustavolyra.spy_price_finder.dto.UrlDto;
import com.gustavolyra.spy_price_finder.dto.product.ProductDto;
import com.gustavolyra.spy_price_finder.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/best-product")
    public ResponseEntity<ProductDto> findBestProduct(@RequestParam String productName,
                                                      @RequestParam(defaultValue = "0.0") Double productMinPrice) throws JsonProcessingException {
        var bestProduct = productService.findBestProduct(productName, productMinPrice);
        return ResponseEntity.ok(bestProduct);
    }

    @PostMapping("/offer-watcher")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> watchOffer(@RequestBody UrlDto dto) {
        productService.watchOffer(dto.url());
        return ResponseEntity.ok("Check your email regularly for updates on the product price");
    }
}
