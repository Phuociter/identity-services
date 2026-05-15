package com.example.indentity.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;// tự khởi tạo contrsuctor có tất cả tham số
import lombok.Builder;// tự khởi tạo builder pattern(tự gán giá trị user khi mapping dữ liệu vd: từ userRequest sang user)
import lombok.Data;// tự khởi tạo getter, setter, toString, equals, hashCode
import lombok.NoArgsConstructor;// tự khởi tạo contrsuctor không tham số
import lombok.experimental.FieldDefaults;// tự khởi tạo tất cả trường là private và final

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)

public class UserUpdateRequest {
    @Size(min=8, message="PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

}
