package com.iremkoc.spring_user_api.service;

import org.springframework.http.ResponseEntity;

import com.iremkoc.spring_user_api.model.LoginRequest;
import com.iremkoc.spring_user_api.model.LoginResponse;
import com.iremkoc.spring_user_api.model.OAuthLoginRequest;
import com.iremkoc.spring_user_api.model.RegisterRequest;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);

    ResponseEntity<String> register(RegisterRequest request);

    LoginResponse oauthLogin(OAuthLoginRequest request);
}
