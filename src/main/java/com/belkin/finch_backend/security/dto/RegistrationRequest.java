package com.belkin.finch_backend.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistrationRequest {

    private String username;
    private String password;
    private String email;
}