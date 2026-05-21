// package com.example.indentity.configuration;

// import com.example.indentity.repository.UserRepository;
// import com.example.indentity.enums.Role;
// import com.example.indentity.entity.User;
// import java.util.HashSet;

// import org.springframework.boot.ApplicationRunner;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class ApplicationInitConfig {
//     ApplicationRunner applicationRunner(UserRepository userRepository) {
//         return args -> {
//             if(userRepository.findByUsername("admn").isEmpty()){
//                 var roles = new HashSet<String>();  
//                 roles.add(Role.ADMIN.name());    

//                 User user = User.Builder()
//                         .username("admin")
//                         .
//             }
//             // Initialize data or perform startup tasks here
//         };
//     }
// }
