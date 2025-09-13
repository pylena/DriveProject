package com.example.simpledriveproject.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class loginDto {

        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }


