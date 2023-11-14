package com.example.springcommerce.service;

import com.example.springcommerce.dto.response.UserResponse;
import com.example.springcommerce.repository.TokenRepository;
import com.example.springcommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
    }

    public UserResponse getUserByToken(String token) {
        var validToken = tokenRepository.findUserByToken(token);
        if (validToken != null) {
            return UserResponse.builder()
                    .id(validToken.getUsers().getId())
                    .firstName(validToken.getUsers().getFirstName())
                    .lastName(validToken.getUsers().getLastName())
                    .username(validToken.getUsers().getUsername())
                    .build();
        } else {
            throw new RuntimeException("Token is not valid");
        }
    }

}
