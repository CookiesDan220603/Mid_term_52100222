package com.example.springcommerce.service;

import com.example.springcommerce.authentication.AuthenticationResponse;
import com.example.springcommerce.authentication.LoginRequest;
import com.example.springcommerce.authentication.RegistrationRequest;
import com.example.springcommerce.model.Cart;
import com.example.springcommerce.model.Role;
import com.example.springcommerce.model.Token;
import com.example.springcommerce.model.User;
import com.example.springcommerce.repository.CartRepository;
import com.example.springcommerce.repository.TokenRepository;
import com.example.springcommerce.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;


    public AuthenticationResponse register(RegistrationRequest request){
        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new RuntimeException("User already exist");
        } else {
            var cart = Cart.builder()
                    .created_date(Date.from(java.time.Instant.now()))
                    .build();
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .cart(cart)
                    .date_created(Date.from(java.time.Instant.now()))
                    .build();

            cart.setUser_id(user);
            var saveUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            saveUserToken(saveUser, jwtToken);
            cartRepository.save(cart);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .username(request.getUsername())
                    .build();
        }
    }

    public AuthenticationResponse authenticate(LoginRequest req, HttpServletRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        var user = userRepository.findByUsername(req.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        Cookie cookie = new Cookie("role", user.getRole().name());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        Cookie userCookie = new Cookie("user", user.getUsername());
        userCookie.setPath("/");
        userCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(userCookie);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(req.getUsername())
                .build();


    }

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .users(user)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validToken.isEmpty()) {
            return;
        }
        validToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validToken);
    }
}
