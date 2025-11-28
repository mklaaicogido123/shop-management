package com.duyphong.shopmanagement.controller.auth;

import com.duyphong.shopmanagement.model.request.auth.LoginRequest;
import com.duyphong.shopmanagement.model.response.auth.LoginResponse;
import com.duyphong.shopmanagement.model.response.auth.RefreshTokenResponse;
import com.duyphong.shopmanagement.service.auth.AuthService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/login")
    @Operation(description = "User Login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(value = "/refreshToken")
    @Operation(description = "Refresh Token")
    ResponseEntity<RefreshTokenResponse> refreshToken(@RequestParam String refreshToken) throws ParseException, JOSEException {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

}
