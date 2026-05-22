package com.example.indentity.controller;

import com.example.indentity.dto.request.ApiResponse;
import com.example.indentity.dto.request.AuthenticationRequest;
import com.example.indentity.dto.response.AuthenticationResponse;
import com.example.indentity.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.text.ParseException;
import org.springframework.web.bind.annotation.*;
import com.nimbusds.jose.JOSEException;
import com.example.indentity.dto.request.IntrospectRequest;
import com.example.indentity.dto.response.IntrospectResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)// làm cho các biến private và final
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder().
        result(result)
        .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException{
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder().
        result(result)
        .build();
    }

}
