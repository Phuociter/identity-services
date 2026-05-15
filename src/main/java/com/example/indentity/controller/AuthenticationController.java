package com.example.indentity.controller;

import com.example.indentity.dto.request.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.*;
import com.example.indentity.dto.request.AuthenticationRequest;
import com.example.indentity.dto.response.AuthenticationResponse;
import com.example.indentity.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)// làm cho các biến private và final
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder().
        result(result)
        .build();
    }
}
