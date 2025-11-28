package com.duyphong.shopmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

/**
 * Cấu hình bảo mật chuẩn cho ứng dụng sử dụng JWT làm Resource Server.
 * Sử dụng JwtDecoder để xử lý việc giải mã và xác thực chữ ký.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Danh sách các endpoints công khai không cần xác thực
    private final String[] PUBLIC_ENDPOINTS = {
            "/users/**",
            "/auth/**",
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    // Khóa bí mật dùng để ký và xác thực JWT
    @Value("${jwt.signKey}")
    protected String SIGN_KEY;

    /**
     * Định nghĩa chuỗi lọc bảo mật cho các HTTP requests.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable); // Vô hiệu hóa CSRF cho API Stateless

        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_ENDPOINTS).permitAll() // Cho phép
                // truy cập
                // công khai
                .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("ADMIN") // Yêu cầu quyền ADMIN
                .anyRequest().authenticated()); // Các request còn lại yêu cầu xác thực

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        return httpSecurity.build();
    }

    /**
     *
     * Nhiệm vụ: Giải mã token, kiểm tra chữ ký (dùng SIGN_KEY) và kiểm tra thời
     * hạn.
     */
    @Bean
    JwtDecoder jwtDecoder() {
        // Tạo SecretKeySpec từ SIGN_KEY và thuật toán HS512
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGN_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    /**
     * Bean JwtAuthenticationConverter.
     * Nhiệm vụ: Chuyển đổi Claims trong JWT thành Granted Authorities của Spring
     * Security.
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        // 1. Cấu hình để lấy Authority từ claim (mặc định là 'scope' hoặc 'scp',
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Loại bỏ tiền tố mặc định (SCOPE_ hoặc ROLE_)
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}