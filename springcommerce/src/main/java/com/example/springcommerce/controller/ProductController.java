package com.example.springcommerce.controller;

import com.example.springcommerce.dto.request.ProductRequest;
import com.example.springcommerce.dto.response.ProductResponse;
import com.example.springcommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }

    @PostMapping("/get")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

//    @GetMapping("/get")
//    public ResponseEntity<?> searchProduct(@RequestParam(required = false) String filter,
//                                           @RequestParam(required = false) Double priceFrom,
//                                           @RequestParam(required = false) Double priceTo) {
//        return ResponseEntity.ok().body(productService.searchProduct(filter, priceFrom, priceTo));
//    }

    @GetMapping("/filter")
    public ResponseEntity<?> search(
            @RequestParam(name = "filter", required = false) String filter
    ) {
        if (filter == null || filter.isEmpty()) {
            return ResponseEntity.ok(productService.getAllProducts());
        } else {
            return ResponseEntity.ok(productService.filter(filter));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest request, @PathVariable("id") String id) {
        return ResponseEntity.ok(productService.updateProduct(request, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable("id") String id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
