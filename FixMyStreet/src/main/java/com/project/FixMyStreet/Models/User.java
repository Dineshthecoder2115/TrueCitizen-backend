package com.project.FixMyStreet.Models;


import com.project.FixMyStreet.Enums.AccountStatus;
import com.project.FixMyStreet.Enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    @Indexed
    private String email;

    private String password;

    private String phoneNumber;

    private Role role;

    private boolean verified;

    private AccountStatus accountStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}


