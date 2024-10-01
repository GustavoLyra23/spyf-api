package com.gustavolyra.spy_price_finder.repository;

import com.gustavolyra.spy_price_finder.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);


}
