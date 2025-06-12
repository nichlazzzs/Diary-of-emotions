package org.example.proj.controller;


import org.example.proj.service.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // TODO: здесь добавить проверку пользователя и пароля из БД
        if ("user".equals(username) && "password".equals(password)) {
            String role = "ROLE_USER";
            String token = jwtUtils.generateJwtToken(username, role);
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}
