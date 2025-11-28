package com.duyphong.shopmanagement.model.response.auth;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String mail;
    private Date expirationTime;

    private Set<String> roles;
}
