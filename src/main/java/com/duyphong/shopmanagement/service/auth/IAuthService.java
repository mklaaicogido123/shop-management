package com.duyphong.shopmanagement.service.auth;

import com.duyphong.shopmanagement.model.request.auth.LoginRequest;
import com.duyphong.shopmanagement.model.response.auth.LoginResponse;
import com.duyphong.shopmanagement.model.response.auth.RefreshTokenResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthService {
    LoginResponse login(LoginRequest loginRequest);

    RefreshTokenResponse refreshToken(String token) throws ParseException, JOSEException;
}
