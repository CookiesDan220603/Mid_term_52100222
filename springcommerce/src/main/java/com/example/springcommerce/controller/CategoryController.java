package com.example.springcommerce.controller;

import com.example.springcommerce.dto.request.CategoryRequest;
import com.example.springcommerce.dto.request.IdRequest;
import com.example.springcommerce.model.Category;
import com.example.springcommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public String addCategory(@RequestBody CategoryRequest request) {
        return categoryService.addCategory(request);
    }

    @PostMapping("/get")
    public Collection<Category> getCategory() {
        return categoryService.getCategories();
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestBody IdRequest id) {
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/update")
    public String updateCategory(@RequestBody CategoryRequest request) {
        return categoryService.updateCategory(request);
    }
    @GetMapping("/filter_by_category")
    public Collection<Category> filterByCategory(@RequestParam("category") String category) {
        return categoryService.filterByCategory(category);
    }
}
