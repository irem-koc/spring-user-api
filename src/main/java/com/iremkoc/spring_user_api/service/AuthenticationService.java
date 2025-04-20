package com.iremkoc.spring_user_api.service;

import com.iremkoc.spring_user_api.model.AuthenticationResponse;
import com.iremkoc.spring_user_api.model.LoginRequest;
import com.iremkoc.spring_user_api.model.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse register(RegisterRequest request);
}
