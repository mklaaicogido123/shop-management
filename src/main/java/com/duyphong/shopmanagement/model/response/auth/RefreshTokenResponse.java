package com.duyphong.shopmanagement.model.response.auth;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RefreshTokenResponse {
    private String token;
    private Date expiryDate;
}
