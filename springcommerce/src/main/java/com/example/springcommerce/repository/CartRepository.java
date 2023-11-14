package com.example.springcommerce.repository;

import com.example.springcommerce.model.Cart;
import com.example.springcommerce.model.Product;
import com.example.springcommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c FROM Cart c WHERE c.user_id.id = :userId")
    Optional<Cart> findByUser_id(@Param("userId") String userId);

    @Query("SELECT c from Cart c WHERE c.user_id.id = ?1")
    Cart findCartByUser_id(String userId);
}
