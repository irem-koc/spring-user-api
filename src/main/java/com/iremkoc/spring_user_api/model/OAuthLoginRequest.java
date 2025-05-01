package com.iremkoc.spring_user_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthLoginRequest {
    private String email;
    private String name;
    private String surname;
    private String provider;
    private String providerId;
}
