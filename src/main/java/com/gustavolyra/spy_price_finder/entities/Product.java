package com.gustavolyra.spy_price_finder.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UUID;
    @Column(nullable = false)
    private String title;
    @Column(unique = true, nullable = false)
    private String link;
    @Column(nullable = false)
    private Double price;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(link, product.link);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(link);
    }
}
