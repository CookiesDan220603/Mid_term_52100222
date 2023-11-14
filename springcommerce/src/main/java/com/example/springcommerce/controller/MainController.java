package com.example.springcommerce.controller;

import com.example.springcommerce.service.CategoryService;
import com.example.springcommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/product")
    public String product(
            @RequestParam(name = "filter", required = false) String filter,
            Model model
    ) {
        model.addAttribute("brands", productService.findAllBrand());
        model.addAttribute("colors", productService.findAllColor());
        model.addAttribute("categories", categoryService.getCategories());
        return "pages/product";
    }

    @GetMapping("/product/{id}")
    public String productDetail() {
        return "pages/product-detail";
    }

    @GetMapping("/cart")
    public String cart() {
        return "pages/cart";
    }

}
