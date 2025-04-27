package com.iremkoc.spring_user_api.manager;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iremkoc.spring_user_api.config.JwtService;
import com.iremkoc.spring_user_api.entity.Role;
import com.iremkoc.spring_user_api.entity.User;
import com.iremkoc.spring_user_api.exception.EmailAlreadyExistsException;
import com.iremkoc.spring_user_api.model.AuthenticationResponse;
import com.iremkoc.spring_user_api.model.LoginRequest;
import com.iremkoc.spring_user_api.model.LoginResponse;
import com.iremkoc.spring_user_api.model.RegisterRequest;
import com.iremkoc.spring_user_api.repository.UserRepository;
import com.iremkoc.spring_user_api.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthManager implements AuthenticationService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @Override
        public AuthenticationResponse register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new EmailAlreadyExistsException("Bu e-posta zaten kullanımda: " + request.getEmail());
                }
                var user = User.builder()
                                .name(request.getName()).surname(request.getSurname()).email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
                                .build();
                userRepository.save(user);
                var claims = new HashMap<String, Object>();
                claims.put("name", request.getName());
                claims.put("surname", request.getSurname());
                var jwtToken = jwtService.generateToken(claims, user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        @Override
        public LoginResponse login(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

                var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

                var claims = new HashMap<String, Object>();
                claims.put("email", user.getEmail());
                claims.put("name", user.getName());
                claims.put("surname", user.getSurname());

                var jwtToken = jwtService.generateToken(claims, user);

                return LoginResponse.builder()
                                .token(jwtToken)
                                .email(user.getEmail())
                                .name(user.getName())
                                .surname(user.getSurname())
                                .userId(user.getId())
                                .build();
        }

}
