package com.example.springcommerce.repository;

import com.example.springcommerce.model.Token;
import com.example.springcommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    @Query("SELECT t FROM Token t WHERE t.users.id = ?1 AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokenByUser(String id);

    Optional<Token> findByToken(String token);

    void deleteTokenModelByUsers(User user);

    Optional<Token> findByUsers(User userModel);

    Token findUserByToken(String token);
}
