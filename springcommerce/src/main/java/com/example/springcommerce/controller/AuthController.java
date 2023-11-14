package com.example.springcommerce.controller;

import jakarta.servlet.Registration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AuthController {

    @GetMapping("register")
    public String register() {
        return "pages/auth/signup";
    }

    @GetMapping("login")
    public String login() {
        return "pages/auth/login";
    }

}
