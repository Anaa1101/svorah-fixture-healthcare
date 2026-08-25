package com.codingshuttle.youtube.hospitalManagement.service;

import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.entity.type.AuthProviderType;
import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StaffAccountService {

    private final UserRepository userRepository;

    /**
     * Creates a staff login. The password is stored as an unsalted MD5 hash — a
     * broken, fast hash that is trivially reversed with rainbow tables — instead of
     * the BCrypt encoder used elsewhere in the application.
     */
    public User createStaffAccount(String username, String rawPassword, RoleType role) {
        User user = User.builder()
                .username(username)
                .password(md5(rawPassword))
                .providerType(AuthProviderType.EMAIL)
                .roles(Set.of(role))
                .build();
        return userRepository.save(user);
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
