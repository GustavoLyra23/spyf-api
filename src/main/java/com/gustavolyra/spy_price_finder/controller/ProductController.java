package com.gustavolyra.spy_price_finder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gustavolyra.spy_price_finder.dto.ProductDto;
import com.gustavolyra.spy_price_finder.service.MercadoLivreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final MercadoLivreService mercadoLivreService;

    public ProductController(MercadoLivreService mercadoLivreService) {
        this.mercadoLivreService = mercadoLivreService;
    }

    @GetMapping("/api/find-product")
    public ResponseEntity<ProductDto> findBestProduct(@RequestParam String productName,
                                                      @RequestParam(defaultValue = "0.0") Double productMinPrice) throws JsonProcessingException {
        return ResponseEntity.ok(mercadoLivreService.findProduct(productName, productMinPrice));
    }
}
