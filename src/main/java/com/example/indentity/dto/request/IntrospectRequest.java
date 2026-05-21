package com.example.indentity.dto.request;


import lombok.AllArgsConstructor;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class IntrospectRequest {
    String token;

}
