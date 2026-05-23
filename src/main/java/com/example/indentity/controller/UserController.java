package com.example.indentity.controller;

import com.example.indentity.dto.request.ApiResponse;
import com.example.indentity.dto.request.UserCreationRequest;
import com.example.indentity.dto.request.UserUpdateRequest;
import com.example.indentity.dto.response.UserResponse;
import com.example.indentity.service.UserService;
import com.example.indentity.entity.User;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<User>builder()
                .result(userService.createUser(request))
                .build();
    }


    @GetMapping
    public ApiResponse<List<User>> getListUser() {
        // var authentication =  SecurityContextHolder.getContext().getAuthentication();
        // log.info("UserName: {}", authentication.getName());
        // authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));


        return ApiResponse.<List<User>>builder()
                .result(userService.getListUser())
                .build();
    }

    @GetMapping("/{userid}")
    public ApiResponse<UserResponse> getUser(@PathVariable String userid) {
       return ApiResponse.<UserResponse>builder()
               .result(userService.getUserById(userid))
               .build();
    }

    @PutMapping("/{userid}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userid, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUserRepository(userid, request))
                .build();
    }


    @DeleteMapping("/{userid}")
    public String deleteUser(@PathVariable String userid) {
        userService.deleteUser(userid);
        return "User deleted successfully";
    }
}
