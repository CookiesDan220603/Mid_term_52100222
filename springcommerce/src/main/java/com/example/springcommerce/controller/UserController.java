package com.example.springcommerce.controller;

import com.example.springcommerce.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.springcommerce.service.UserService;
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/get_user_by_token")
    public ResponseEntity<UserResponse> getUserByToken(@RequestBody String token) {
        return ResponseEntity.ok().body(userService.getUserByToken(token));
    }
}
