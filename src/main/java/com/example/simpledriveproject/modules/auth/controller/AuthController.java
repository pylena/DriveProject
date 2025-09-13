package com.example.simpledriveproject.modules.auth.controller;

import com.example.simpledriveproject.modules.auth.dto.loginDto;
import com.example.simpledriveproject.modules.auth.service.JwtGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final JwtGeneratorService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody loginDto request) {

        if ("admin".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            String token = jwtService.generateToken(request.getUsername());
            return ResponseEntity.ok( "token:"+ token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid");
    }

}
