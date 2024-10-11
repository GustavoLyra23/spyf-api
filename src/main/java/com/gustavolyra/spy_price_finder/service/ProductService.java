package com.gustavolyra.spy_price_finder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gustavolyra.spy_price_finder.dto.product.ProductDto;
import com.gustavolyra.spy_price_finder.entities.Product;
import com.gustavolyra.spy_price_finder.repository.ProductRepository;
import com.gustavolyra.spy_price_finder.service.exceptions.ResourceNotFoundException;
import com.gustavolyra.spy_price_finder.service.util.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final MercadoLibreService mercadoLibreService;
    private final AmazonScrappingService amazonScrappingService;
    private final ProductRepository productRepository;
    private final ScrapService scrapService;

    public ProductService(MercadoLibreService mercadoLibreService, AmazonScrappingService amazonScrappingService, ProductRepository productRepository, ScrapService scrapService) {
        this.mercadoLibreService = mercadoLibreService;
        this.amazonScrappingService = amazonScrappingService;
        this.productRepository = productRepository;
        this.scrapService = scrapService;
    }

    public ProductDto findBestProduct(String productName, Double productMinPrice) throws JsonProcessingException {
        ProductDto mercadoLibreProduct = mercadoLibreService.findProduct(productName, productMinPrice);
        ProductDto amazonProduct = amazonScrappingService.scrapAmazonProduct(productName, productMinPrice);

        if (mercadoLibreProduct == null && amazonProduct == null) {
            throw new ResourceNotFoundException("No product found");
        } else if (mercadoLibreProduct == null) {
            return amazonProduct;
        } else if (amazonProduct == null) {
            return mercadoLibreProduct;
        }

        return mercadoLibreProduct.price() < amazonProduct.price() ? mercadoLibreProduct : amazonProduct;
    }

    @Transactional
    public void watchOffer(String url) {
        var optional = productRepository.findByLink(url);
        if (optional.isEmpty()) {
            Product product = new Product();
            var productdto = scrapService.scrapPageFromUrl(url);
            product.setLink(url);
            product.setTitle(productdto.title());
            product.setPrice(productdto.price());
            product.getUsers().add(UserUtil.findUserFromAuthenticationContext());
            productRepository.save(product);
        } else {
            Product product = optional.get();
            product.getUsers().add(UserUtil.findUserFromAuthenticationContext());
            productRepository.save(product);
        }
    }

}

