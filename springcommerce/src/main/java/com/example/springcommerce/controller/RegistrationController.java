package com.example.springcommerce.controller;

import com.example.springcommerce.authentication.AuthenticationResponse;
import com.example.springcommerce.authentication.LoginRequest;
import com.example.springcommerce.authentication.RegistrationRequest;
import com.example.springcommerce.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest req, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(registrationService.authenticate(req, request, response));
    }

}
