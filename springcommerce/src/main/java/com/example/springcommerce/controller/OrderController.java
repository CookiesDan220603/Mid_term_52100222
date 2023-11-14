package com.example.springcommerce.controller;

import com.example.springcommerce.dto.request.OrderRequest;
import com.example.springcommerce.dto.response.OrderResponse;
import com.example.springcommerce.dto.response.ProductResponse;
import com.example.springcommerce.model.Order;
import com.example.springcommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public String addOrder(@RequestBody OrderRequest request) {
        orderService.addOrder(request);
        return "success";
    }

    @GetMapping("/get")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody OrderRequest request, @PathVariable("id") String id) {
        return ResponseEntity.ok(orderService.updateOrder(request, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable("id") String id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

    @PostMapping("/checkout/{id}")
    public Double checkout(@RequestBody OrderRequest request, @PathVariable("id") String id) {
        return orderService.checkout(request, id);
    }
}
