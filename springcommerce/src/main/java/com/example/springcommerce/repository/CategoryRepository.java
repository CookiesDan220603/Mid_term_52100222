package com.example.springcommerce.repository;

import com.example.springcommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
//    Optional<Category> findById(String id);

    Category findCategoriesById(String id);

    Category findByName(String name);
    void deleteById(String id);
}
