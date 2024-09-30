package com.gustavolyra.spy_price_finder.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface MercadoLibreConfig {

    @GetExchange("/sites/MLB/search")
    ResponseEntity<String> getSearchResultsPaginated(@RequestParam() String q,
                                                     @RequestParam() int offset,
                                                     @RequestParam() int limit);
}
