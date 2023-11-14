package com.example.springcommerce.authentication;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private  String username;
    private  String password;
}
