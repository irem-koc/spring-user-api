package com.iremkoc.spring_user_api.manager;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iremkoc.spring_user_api.config.JwtService;
import com.iremkoc.spring_user_api.entity.Role;
import com.iremkoc.spring_user_api.entity.User;
import com.iremkoc.spring_user_api.exception.EmailAlreadyExistsException;
import com.iremkoc.spring_user_api.model.LoginRequest;
import com.iremkoc.spring_user_api.model.LoginResponse;
import com.iremkoc.spring_user_api.model.OAuthLoginRequest;
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
        public ResponseEntity<String> register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new EmailAlreadyExistsException("Bu e-posta zaten kullanımda: " + request.getEmail());
                }
                var user = User.builder()
                                .name(request.getName()).surname(request.getSurname()).email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
                                .build();
                userRepository.save(user);
                return ResponseEntity.ok("Kayıt başarılı");
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

        @Override
        public LoginResponse oauthLogin(OAuthLoginRequest request) {
                Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

                User user = userOptional.orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(request.getEmail());
                        newUser.setName(request.getName());
                        newUser.setSurname(request.getSurname());
                        newUser.setProvider(request.getProvider());
                        newUser.setProviderId(request.getProviderId());
                        newUser.setRole(Role.USER);
                        return userRepository.save(newUser);
                });

                var claims = new HashMap<String, Object>();
                claims.put("email", user.getEmail());
                claims.put("name", user.getName());
                claims.put("surname", user.getSurname());

                var jwtToken = jwtService.generateToken(claims, user);

                return new LoginResponse(jwtToken, user.getEmail(), user.getName(), user.getSurname(),
                                user.getId());
        }

}
