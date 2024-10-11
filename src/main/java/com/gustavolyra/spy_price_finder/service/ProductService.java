package com.gustavolyra.spy_price_finder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gustavolyra.spy_price_finder.dto.product.ProductDto;
import com.gustavolyra.spy_price_finder.entities.Product;
import com.gustavolyra.spy_price_finder.repository.ProductRepository;
import com.gustavolyra.spy_price_finder.service.util.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final MercadoLibreService mercadoLibreService;
    private final AmazonService amazonService;
    private final ProductRepository productRepository;
    private final ScrapService scrapService;

    public ProductService(MercadoLibreService mercadoLibreService, AmazonService amazonService, ProductRepository productRepository, ScrapService scrapService) {
        this.mercadoLibreService = mercadoLibreService;
        this.amazonService = amazonService;
        this.productRepository = productRepository;
        this.scrapService = scrapService;
    }

    public List<ProductDto> findBestProduct(String productName, Double productMinPrice) throws JsonProcessingException {
        String mercadoLibreProduct = productName.trim();
        var mercadoLibreList = mercadoLibreService.findProduct(mercadoLibreProduct, productMinPrice);
        String amazonProduct = productName.replace(" ", "%20");
        var amazonList = amazonService.scrapAmazonProduct(amazonProduct, productMinPrice);
        List<ProductDto> bestProducts = new ArrayList<>();

        if (mercadoLibreList == null && amazonList == null) {
            return bestProducts;
        } else if (mercadoLibreList == null) {
            return amazonList;
        } else if (amazonList == null) {
            return mercadoLibreList;
        }

        bestProducts.addAll(mercadoLibreList);
        bestProducts.addAll(amazonList);
        return bestProducts.stream().sorted(Comparator.comparingDouble(ProductDto::price))
                .limit(5).toList();
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

