package com.gustavolyra.spy_price_finder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebConfig {

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory() {
        WebClient builder = WebClient.builder().baseUrl("https://api.mercadolibre.com").build();
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(builder)).build();
    }

    @Bean
    public MercadoLibreConfig githubClient(HttpServiceProxyFactory factory) {
        return factory.createClient(MercadoLibreConfig.class);
    }

}
