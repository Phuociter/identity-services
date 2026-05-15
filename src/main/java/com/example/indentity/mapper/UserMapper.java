package com.example.indentity.mapper;

import com.example.indentity.dto.request.UserCreationRequest;
import com.example.indentity.dto.request.UserUpdateRequest;
import com.example.indentity.dto.response.UserResponse;
import com.example.indentity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
    UserResponse toUserResponse(User user);
}
