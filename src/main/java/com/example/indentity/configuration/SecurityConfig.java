package com.example.indentity.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;





@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String [] PUBLIC_POST_ENDPOINTS = {"/users", 
            "/auth/token", "/auth/introspect"};

    @Value("${jwt.secret}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
            request.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
            .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> 
            oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);// tắt CSRF để cho phép POST request từ client


        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");

        return NimbusJwtDecoder.
        withSecretKey(secretKeySpec)
        .macAlgorithm(MacAlgorithm.HS512)// xác định thuật toán mã hóa HMAC-SHA512 cho việc ký token JWT
        .build();
    }

}