package com.example.springcommerce.service;

import com.example.springcommerce.dto.request.OrderRequest;
import com.example.springcommerce.dto.response.OrderResponse;
import com.example.springcommerce.dto.response.ProductResponse;
import com.example.springcommerce.model.Cart;
import com.example.springcommerce.model.Order;
import com.example.springcommerce.model.Product;
import com.example.springcommerce.model.User;
import com.example.springcommerce.repository.CartRepository;
import com.example.springcommerce.repository.OrderRepository;
import com.example.springcommerce.repository.ProductRepository;
import com.example.springcommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public OrderResponse convertOrder(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .products(order.getProducts().stream().map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .build()).toList())
                .price(order.getPrice())
                .build();
    }

    @Transactional
    public String addOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
//        Cart cart = cartRepository.findByUser_id(request.getUserId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<Product> products = request.getProducts().stream().map(productId -> productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"))).toList();
        Order order = Order.builder()
                .user(user)
                .products(new HashSet<>(products))
                .price(products.stream().reduce(0.0, (a, b) -> a + b.getPrice(), Double::sum))
                .build();
        orderRepository.save(order);

        return "success";
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::convertOrder).toList();
    }

    public OrderResponse updateOrder(OrderRequest request, String id) {
        var order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        List<Product> products = request.getProducts().stream().map(productId -> productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"))).toList();
        if (order != null){
            order.setProducts(new HashSet<>(products));
            order.setPrice(products.stream().reduce(0.0, (a, b) -> a + b.getPrice(), Double::sum));
            order.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
            orderRepository.save(order);
            return convertOrder(order);
        }
        return null;
    }

    public OrderResponse deleteOrder(String id) {
        var order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if (order != null){
            OrderResponse newOrder = convertOrder(order);
            orderRepository.delete(order);
            return newOrder;
        }
        return null;
    }

    public Double checkout(OrderRequest request, String id) {
//        List<String> products = request.getProducts().stream().toList();
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        List<Product> products = order.getProducts().stream().toList();
        Double totalPrice = 0.0;
        totalPrice = products.stream().reduce(0.0, (a, b) -> a + b.getPrice(), Double::sum);
        return totalPrice;
    }
}
