package com.duyphong.shopmanagement.config;

import com.duyphong.shopmanagement.entity.user.UserEntity;
import com.duyphong.shopmanagement.enums.Role;
import com.duyphong.shopmanagement.repository.user.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class InitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUserName("admin1234").isEmpty()) {
                String hashPassword = passwordEncoder.encode("admin1234");
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());
                UserEntity user = UserEntity.builder().
                        userName("admin1234")
                        .password(hashPassword)
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin user created by default password");
            }
        };
    }
}
