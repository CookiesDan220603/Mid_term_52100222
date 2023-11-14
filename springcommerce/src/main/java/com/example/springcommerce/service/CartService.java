package com.example.springcommerce.service;

import com.example.springcommerce.dto.request.AddProductRequest;
import com.example.springcommerce.dto.response.CartResponse;
import com.example.springcommerce.dto.response.ProductResponse;
import com.example.springcommerce.model.*;
import com.example.springcommerce.repository.CartProductRepository;
import com.example.springcommerce.repository.CartRepository;
import com.example.springcommerce.repository.ProductRepository;
import com.example.springcommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartProductRepository cartProductRepository;


    public void addToCart(String id_product, String id) {
        System.out.println("id_product: " + id_product);
        System.out.println("id: " + id);
        Cart cart = cartRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cart not found")
        );
//        User user = cart.getUser_id();
//        List<Product> products = request.getProductIds().stream().map(productId ->
//                productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"))
//        ).toList();
//        cart.setProducts(products);
        var product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        CartProduct productEntity = cartProductRepository.findByCartIdAndProductId(cart.getId(), product.getId()).orElse(null);
        if (productEntity == null) {
            productEntity = CartProduct.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .build();
        } else {
            productEntity.setQuantity(productEntity.getQuantity() + 1);
        }
        cartProductRepository.save(productEntity);
    }

    public CartResponse getCartByUser(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Cart cart = cartRepository.findCartByUser_id(user.getId());
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        return CartResponse.builder()
                .id(cart.getId())
                .user_id(cart.getUser_id().getId())
                .created_date(cart.getCreated_date())
                .updated_date(cart.getUpdated_date())
                .products(cart.getCartProducts().stream().map(cartProduct -> {
                    Product product = cartProduct.getProduct();
                    return ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrice())
                            .description(product.getDescription())
                            .quantity(cartProduct.getQuantity())
                            .build();
                }).toList())
                .build();
    }

    public CartResponse add(AddProductRequest request) {
        var products = productRepository.findAllByIdIn(request.getProductIds());

        if (CollectionUtils.isEmpty(products)) {
            throw new RuntimeException("Product not found");
        }

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        var cart = cartRepository.findByUser_id(user.getId()).orElseThrow(
                () -> new RuntimeException("Cart not found")
        );

        var cartProducts = cartProductRepository.findByCartId(cart.getId());
        products.forEach(product -> {
            var cartProduct = cartProducts.stream().filter(cartProduct1 -> cartProduct1.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
            if (cartProduct == null) {
                cartProduct = CartProduct.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(1)
                        .build();
            } else {
                cartProduct.setQuantity(cartProduct.getQuantity() + 1);
            }
            cartProductRepository.save(cartProduct);
        });

        return CartResponse.builder()
                .id(cart.getId())
                .user_id(cart.getUser_id().getId())
                .created_date(cart.getCreated_date())
                .updated_date(cart.getUpdated_date())
                .products(cart.getCartProducts().stream().map(cartProduct -> {
                    Product product = cartProduct.getProduct();
                    return ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrice())
                            .description(product.getDescription())
                            .quantity(cartProduct.getQuantity())
                            .build();
                }).toList())
                .build();
    }

    @Transactional
    public String delete(String id, String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        var cart = cartRepository.findByUser_id(user.getId()).orElseThrow(
                () -> new RuntimeException("Cart not found")
        );

        cartProductRepository.deleteByCartIdAndProductId(cart.getId(), id);

        return "success";
    }
}
