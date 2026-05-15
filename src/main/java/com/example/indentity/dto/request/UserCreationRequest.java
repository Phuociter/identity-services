package com.example.indentity.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min=3, max=100, message="USERNAME_INVALID")
    String username;

    @Size(min=8, message="PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName; 

    LocalDate dob;

}
