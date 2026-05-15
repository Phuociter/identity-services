package com.example.indentity.service;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.example.indentity.dto.request.AuthenticationRequest;
import com.example.indentity.exception.AppException;
import com.example.indentity.exception.ErrorCode;
import com.example.indentity.repository.UserRepository;
import com.example.indentity.dto.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JWSObject;// tao token JWT
import com.nimbusds.jwt.JWTClaimsSet;// tao payload cho token
import com.nimbusds.jose.JWSHeader;// tao header cho token
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;// xac dinh thuat toan ma hoa cho token
import com.nimbusds.jose.Payload;// tao payload cho token
import com.nimbusds.jose.crypto.MACSigner;// tao sign
import java.time.Instant;


import lombok.extern.slf4j.Slf4j;// tao log

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    
    UserRepository userRepository;
    @NonFinal//anotation cua lombok de cho phep thay doi gia tri cua bien
    protected static final String SIGNER_KEY = "7jykC9LD/jWovKd2wo9bHcTHsb9dVjvuYHVHKLeodUcPqCZD6FdsZYh9JHZ0ltD+vjKsgpO14GCVUSPLyrrGUA==";

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername()).
        orElseThrow(()-> new AppException(ErrorCode.USER_NOTEXISTS));
        
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        
        var token = generateToken(user.getUsername());
        //verify Token 28:30
        return AuthenticationResponse.builder()
            .authenticated(true)
            .token(token)
            .build();
    }

// cau truc mot token JWT bao gom header, payload va signature
    private String generateToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(username)
            .issuer("intern.com")
            .issueTime(new Date())
            .expirationTime(new Date(
                Instant.now().plus(1,ChronoUnit.HOURS).toEpochMilli())// token sẽ hết hạn sau 1 giờ
            )
            .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());// payload là dữ liệu của token được mã hóa dưới dạng JSON

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();// serialize() sẽ trả về token dưới dạng chuỗi
        } catch (JOSEException e) {
            log.error("can not create token", e);
            throw new RuntimeException("Error signing the token", e);
        }

    }
}
