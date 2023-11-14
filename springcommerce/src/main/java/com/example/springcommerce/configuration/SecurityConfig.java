package com.example.springcommerce.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtAuthenticateFilter authenticationFilter;

    private static final String[] public_path = new String[]{
            "/admin/**",
            "/category/**",
            "/product",
            "/product/**",
            "/cart/**",
            "/cart/add-to-cart/**",
            "/add-to-cart",
            "/order/**",
            "/register",
            "/login",
            "/api/auth/**",
            "/api/**",
            "/**",
            "/images/**",
            "/css/**",
            "/js/**",
            "webjars/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorise -> authorise
                        .requestMatchers(public_path)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;

        return http.build();
    }
}
