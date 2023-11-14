package com.example.springcommerce.repository;

import com.example.springcommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
//    Optional<User> findByCartId(String cartId);

    boolean existsUserByUsername(String username);
}
