package com.gustavolyra.spy_price_finder.repository;

import com.gustavolyra.spy_price_finder.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByLink(String link);
}
