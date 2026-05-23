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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import com.example.indentity.enums.Role;


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
            request.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()// cho phép tất cả người dùng truy cập endpoint POST /users, /auth/token và /auth/introspect mà không cần xác thực
                    .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())// chỉ cho phép người dùng có vai trò ADMIN truy cập endpoint GET /users
            .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> 
            oauth2.jwt(jwtConfigurer -> 
                jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                ));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);// tắt CSRF để cho phép POST request từ client

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");

        return NimbusJwtDecoder.
        withSecretKey(secretKeySpec)
        .macAlgorithm(MacAlgorithm.HS512)// xác định thuật toán mã hóa HMAC-SHA512 cho việc ký token JWT
        .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }



}