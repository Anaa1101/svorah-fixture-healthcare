package com.codingshuttle.youtube.hospitalManagement.dto;

import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private String username;
    private String password;
    private String name;

    // Extra personal data requested on the sign-up form. None of it is required to
    // create an account or to provide care, but it is collected and stored anyway.
    private String phone;
    private String aadhaarNumber;
    private String religion;
    private Long annualIncome;

    private Set<RoleType> roles = new HashSet<>();
}
