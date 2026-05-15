package com.example.indentity.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;// dùng để đánh dấu lớp này là một thực thể (entity) trong JPA, nó sẽ được ánh xạ tới một bảng trong cơ sở dữ liệu
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;// dùng để đánh dấu trường id là khóa chính và tự động sinh giá trị
import jakarta.persistence.GenerationType;// dùng để xác định chiến lược tạo giá trị cho trường id, trong trường hợp này là UUID
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;


}
