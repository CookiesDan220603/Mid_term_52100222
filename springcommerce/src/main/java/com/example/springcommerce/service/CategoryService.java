package com.example.springcommerce.service;

import com.example.springcommerce.dto.request.CategoryRequest;
import com.example.springcommerce.dto.request.IdRequest;
import com.example.springcommerce.model.Category;
import com.example.springcommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Collection<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public String addCategory(CategoryRequest request){
       var category = categoryRepository.findByName(request.getName());
       if (category == null){
           category = Category.builder()
                   .name(request.getName())
                   .description(request.getDescription())
                   .created_date(Date.from(java.time.Instant.now()))
                   .updated_date(null)
                   .build();
           categoryRepository.save(category);
           return "success";
       }
       return "failed";
    }

    public String updateCategory(CategoryRequest request) {
        var category = categoryRepository.findByName(request.getName());
        if (category != null) {
            category.setName(request.getName());
            category.setDescription(request.getDescription());
            category.setUpdated_date(Date.from(java.time.Instant.now()));
            categoryRepository.save(category);
            return "success";
        } else {
            return "failed";
        }

    }

    public String deleteCategory(IdRequest request) {
        var category = categoryRepository.findById(request.getId());
        if (category.isPresent()) {
            categoryRepository.deleteById(request.getId());
            return "success";
        }
        return "failed";
    }
    public Collection<Category> filterByCategory(String category) {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
