package com.example.springcommerce.repository;

import com.example.springcommerce.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, String> {
    Optional<CartProduct> findByCartIdAndProductId(String cartId, String productId);
    List<CartProduct> findByCartId(String cartId);
    void deleteByCartIdAndProductId(String cartId, String productId);
}
