package com.example.springcommerce.controller;

import com.example.springcommerce.dto.request.AddProductRequest;
import com.example.springcommerce.dto.response.CartResponse;
import com.example.springcommerce.model.Cart;
import com.example.springcommerce.model.Product;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.springcommerce.service.CartService;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add-to-cart/{id}")
    public void addToCart(@RequestBody String id_product, @PathVariable("id") String id) {
        cartService.addToCart(id_product, id);
    }

//    @PostMapping("/checkout/{id}")
//    public void checkout(@PathVariable("id") String id) {
//        cartService.checkout(id);
//    }

    @GetMapping("/get_cart_by_user")
    public ResponseEntity<CartResponse> getCartByUser(@RequestParam(name = "id", defaultValue = "") String id) {
        return ResponseEntity.ok().body(cartService.getCartByUser(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(
            @RequestBody AddProductRequest request
    ) {
        return ResponseEntity.ok().body(cartService.add(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable("id") String id,
            @RequestParam(name = "username", defaultValue = "") String username
    ) {
        return ResponseEntity.ok().body(cartService.delete(id, username));
    }
}
