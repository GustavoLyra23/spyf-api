package com.gustavolyra.spy_price_finder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gustavolyra.spy_price_finder.dto.product.ProductDto;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final MercadoLibreService mercadoLibreService;
    private final AmazonScrappingService amazonScrappingService;


    public ProductService(MercadoLibreService mercadoLibreService, AmazonScrappingService amazonScrappingService) {
        this.mercadoLibreService = mercadoLibreService;
        this.amazonScrappingService = amazonScrappingService;
    }

    public ProductDto findBestProduct(String productName, Double productMinPrice) throws JsonProcessingException {
        ProductDto mercadoLibreProduct = mercadoLibreService.findProduct(productName, productMinPrice);
        ProductDto amazonProduct = amazonScrappingService.scrapAmazonProduct(productName, productMinPrice);

        if (mercadoLibreProduct == null) {
            return amazonProduct;
        } else if (amazonProduct == null) {
            return mercadoLibreProduct;
        }

        return mercadoLibreProduct.price() < amazonProduct.price() ? mercadoLibreProduct : amazonProduct;
    }


    public void watchOffer(String url) {
        //TODO
    }
}
