package com.example.indentity.service;


import com.example.indentity.entity.User;
import com.example.indentity.enums.Role;
import com.example.indentity.exception.AppException;
import com.example.indentity.exception.ErrorCode;
import com.example.indentity.mapper.UserMapper;
import com.example.indentity.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.indentity.dto.request.UserCreationRequest;
import com.example.indentity.dto.request.UserUpdateRequest;
import com.example.indentity.dto.response.UserResponse;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor// tự động tạo constructor với tất cả các trường được khai báo là final hoặc được đánh dấu với @NonNull
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)//tự động đặt tất cả các trường là private và final, giúp giảm bớt boilerplate code
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    
    public User createUser(UserCreationRequest request) {
        

        if(userRepository.existsByUsername(request.getUsername()))
        throw new AppException(ErrorCode.USER_EXISTS);

        User user = userMapper.toUser(request);
        
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        HashSet<String> role = new HashSet<>();
        role.add(Role.USER.name());
        user.setRole(role);

        user.setRole(role);

        return userRepository.save(user);
    }

    public List<User> getListUser() {
        return userRepository.findAll();
    }

    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found")));
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
    
    public UserResponse updateUserRepository(String userid, UserUpdateRequest request) {

        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("user not found"));

        userMapper.updateUser(user, request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userid){
        userRepository.deleteById(userid);
    }
}
