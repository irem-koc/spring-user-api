package com.iremkoc.spring_user_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iremkoc.spring_user_api.manager.AuthManager;
import com.iremkoc.spring_user_api.model.LoginRequest;
import com.iremkoc.spring_user_api.model.LoginResponse;
import com.iremkoc.spring_user_api.model.OAuthLoginRequest;
import com.iremkoc.spring_user_api.model.RegisterRequest;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthManager authManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {
        return authManager.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authManager.login(request));
    }

    @PostMapping("/oauth-login")
    public ResponseEntity<LoginResponse> oauthLogin(
            @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(authManager.oauthLogin(request));
    }
}
