package com.example.springcommerce.repository;

import com.example.springcommerce.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String>{
//    Product findById(String id);

    Product findByName(String name);
    List<Product> findByCategory_id(String category_id);

    @Query("SELECT p FROM Product p " +
            "WHERE p.name LIKE %?1% " +
            "OR p.description LIKE %?1% " +
            "OR p.category.name LIKE %?1% "
    )
    List<Product> searchAll(String keyword);

    @Query("SELECT p FROM Product p " +
            "WHERE p.name LIKE %?1% " +
            "OR p.description LIKE %?1% " +
            "OR p.category.name LIKE %?1% " +
            "AND p.price >= ?2 " +
            "AND p.price <= ?3"
    )
    List<Product> searchAll(String keyword, double minPrice, double maxPrice);

    @Query("SELECT p FROM Product p " +
            "WHERE p.price >= ?1 " +
            "AND p.price <= ?2"
    )
    List<Product> searchAll(double minPrice, double maxPrice);

    @Query("SELECT p FROM Product p " +
            "WHERE p.name LIKE %?1% " +
            "OR p.description LIKE %?1% " +
            "OR p.category.name LIKE %?1% " +
            "AND p.price >= ?2"
    )
    List<Product> searchAllMinPrice(String keyword, double minPrice);

    @Query("SELECT p FROM Product p " +
            "WHERE p.name LIKE %?1% " +
            "OR p.description LIKE %?1% " +
            "OR p.category.name LIKE %?1% " +
            "AND p.price <= ?2"
    )
    List<Product> searchAllMaxPrice(String keyword, double maxPrice);

    @Query("SELECT p FROM Product p " +
            "WHERE p.price >= ?1"
    )
    List<Product> searchAllMinPrice(double minPrice);


    @Query("SELECT p FROM Product p " +
            "WHERE p.price <= ?1"
    )
    List<Product> searchAllMaxPrice(double maxPrice);

    List<Product> findAllByIdIn(List<String> ids);

    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findDistinctBrand();

    @Query("SELECT DISTINCT p.color FROM Product p")
    List<String> findDistinctColor();

    List<Product> findAll(Specification<Product> spec);
}
